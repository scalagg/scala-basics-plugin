package gg.scala.basics.plugin.disguise

import dev.iiahmed.disguise.Disguise
import dev.iiahmed.disguise.DisguiseManager
import dev.iiahmed.disguise.DisguiseResponse
import gg.scala.aware.AwareBuilder
import gg.scala.aware.codec.codecs.interpretation.AwareMessageCodec
import gg.scala.aware.message.AwareMessage
import gg.scala.aware.thread.AwareThreadContext
import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.basics.plugin.disguise.events.AsyncPlayerDisguiseEvent
import gg.scala.basics.plugin.disguise.sessionserver.SessionServerResponse
import gg.scala.basics.plugin.profile.BasicsProfileService
import gg.scala.basics.plugin.settings.defaults.values.StateSettingValue
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.lemon.handler.PlayerHandler
import gg.scala.lemon.handler.RankHandler
import gg.scala.lemon.util.QuickAccess
import me.lucko.helper.Events
import me.lucko.helper.Schedulers
import me.lucko.helper.utils.Players
import net.evilblock.cubed.ScalaCommonsSpigot
import net.evilblock.cubed.serializers.Serializers
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Tasks
import net.evilblock.cubed.util.bukkit.uuid.UUIDUtil
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.metadata.FixedMetadataValue
import java.net.URL
import java.util.UUID
import java.util.concurrent.CompletableFuture

/**
 * @author GrowlyX
 * @since 2/5/2023
 */
@Service
object DisguiseService
{
    @Inject
    lateinit var plugin: ScalaBasicsPlugin

    @Inject
    lateinit var audiences: BukkitAudiences

    private val provider by lazy {
        DisguiseManager.getProvider()
    }

    fun provider() = provider

    @Configure
    fun configure()
    {
        DisguiseManager.setPlugin(plugin)
        provider

        val aware = AwareBuilder
            .of<AwareMessage>("disguise-pipe")
            .codec(AwareMessageCodec)
            .build()

        aware.listen("force-undisguise") {
            val uniqueId = UUID.fromString(retrieve<String>("uniqueId"))
            val bukkitPlayer = Bukkit.getPlayer(uniqueId)
                ?: return@listen

            undisguise(bukkitPlayer, postUnDisguise = {
                bukkitPlayer.sendMessage("${CC.RED}You were forcefully undisguised as your disguise's real account logged on the server.")
            })
        }
        aware.connect()

        Schedulers
            .async()
            .runRepeating(Runnable {
                Players
                    .all()
                    .filter { it.hasMetadata("disguised") }
                    .forEach { player ->
                        audiences
                            .player(player)
                            .sendActionBar(
                                LegacyComponentSerializer
                                    .legacySection()
                                    .deserialize(
                                        "${CC.GREEN}You are currently nicked! ${CC.GRAY}(/unnick)"
                                    )
                            )
                    }
            }, 0L, 10L)

        Events
            .subscribe(PlayerJoinEvent::class.java)
            .handler {
                getUsedProfile(it.player.uniqueId)
                    .thenComposeAsync { usedProfile ->
                        if (usedProfile == null)
                        {
                            return@thenComposeAsync CompletableFuture
                                .completedFuture(null)
                        }

                        AwareMessage.of(
                            "force-undisguise",
                            aware,
                            "uniqueId" to usedProfile
                        ).publish(
                            AwareThreadContext.SYNC,
                            "disguise-pipe"
                        )

                        CompletableFuture.completedFuture(null)
                    }
                    .thenComposeAsync { _ ->
                        getApplicantProfileMapping(it.player.uniqueId)
                            .thenApplyAsync { response ->
                                if (response == null)
                                {
                                    val profile = BasicsProfileService
                                        .find(it.player)
                                        ?: return@thenApplyAsync

                                    val autoDisguise = profile.setting(
                                        id = "auto_disguise",
                                        default = StateSettingValue.ENABLED
                                    )

                                    if (
                                        autoDisguise == StateSettingValue.ENABLED &&
                                        it.player.hasPermission("scbasics.disguise.automatic")
                                    )
                                    {
                                        disguiseWithRandomSelection(
                                            player = it.player,
                                            postDisguise = { _ ->
                                                it.player.sendMessage(
                                                    "${CC.GREEN}You've logged onto the server with a new identity due to your automatic nick setting."
                                                )
                                            }
                                        ).join()
                                    }

                                    return@thenApplyAsync
                                }

                                completeProfileApplication(it.player, response) {
                                    it.player.sendMessage(
                                        "${CC.GREEN}You've logged onto this server with a fake identity due to your active nick."
                                    )
                                }
                            }
                    }
            }
    }

    private fun loadSessionServerResponse(disguiseTarget: UUID) =
        getExistingManualDisguiseProfile(disguiseTarget)
            .thenCompose {
                if (it != null)
                    return@thenCompose CompletableFuture
                        .completedFuture(it)

                val sessionServerResp = Serializers
                    .gson
                    .fromJson(
                        URL("https://sessionserver.mojang.com/session/minecraft/profile/$disguiseTarget?unsigned=false")
                            .readText(),
                        SessionServerResponse::class.java
                    )
                    ?: return@thenCompose CompletableFuture
                        .completedFuture(null)

                cacheManualDisguiseProfile(disguiseTarget, sessionServerResp)
                    .thenApplyAsync {
                        sessionServerResp
                    }
            }

    fun runUnDisguisePipeline(
        applicant: UUID,
        applicantNotDisguised: () -> Unit,
        unDisguiseComplete: () -> Unit,
    ) =
        getApplicantProfileMapping(applicant)
            .thenCompose {
                if (it == null)
                {
                    applicantNotDisguised()
                    return@thenCompose CompletableFuture
                        .completedFuture(null)
                }

                val profileId = UUIDUtil
                    .formatUUID(it.id)!!

                setProfileNoLongerInUse(profileId)
                    .thenCompose {
                        removeApplicantProfileMapping(applicant)
                    }
                    .thenRun(unDisguiseComplete)
            }

    fun runDisguisePipelineWithRandomSelection(
        applicant: UUID,
        profileInUse: (SessionServerResponse) -> Unit,
        profileApplicationComplete: (SessionServerResponse) -> Unit
    ) = runDisguisePipeline(
        applicant = applicant,
        serverResponseFuture = {
            randomDisguiseProfile()
        },
        profileInUse = profileInUse,
        profileApplicationComplete = profileApplicationComplete
    )

    fun runDisguisePipelineWithManualSelection(
        applicant: UUID, disguiseTarget: UUID,
        profileNoResponse: () -> Unit,
        profileInUse: (SessionServerResponse) -> Unit,
        profileApplicationComplete: (SessionServerResponse) -> Unit
    ) = runDisguisePipeline(
        applicant = applicant,
        serverResponseFuture = {
            loadSessionServerResponse(disguiseTarget)
                .thenApplyAsync {
                    if (it == null)
                    {
                        profileNoResponse()
                        throw IllegalStateException("Manual selection is null")
                    }

                    disguiseTarget.toString() to it
                }
        },
        profileInUse = profileInUse,
        profileApplicationComplete = profileApplicationComplete
    )

    private fun runDisguisePipeline(
        applicant: UUID,
        serverResponseFuture: () -> CompletableFuture<Pair<String, SessionServerResponse>>,
        profileInUse: (SessionServerResponse) -> Unit,
        profileApplicationComplete: (SessionServerResponse) -> Unit
    ) = serverResponseFuture()
        .thenCompose {
            isProfileInUse(it.second)
                .thenCompose context@{ used ->
                    if (used)
                    {
                        profileInUse(it.second)
                        return@context CompletableFuture
                            .completedFuture(null)
                    }

                    val event = AsyncPlayerDisguiseEvent(
                        player = Bukkit.getPlayer(applicant),
                        profile = it.second
                    )

                    Bukkit.getPluginManager().callEvent(event)

                    if (event.isCancelled)
                    {
                        // We're assuming the event handled anything needed for cancellation
                        return@context CompletableFuture
                            .completedFuture(null)
                    }

                    setDisguiseProfileInUseFor(applicant, it.second)
                        .thenCompose { _ ->
                            mapApplicantToProfile(
                                applicant,
                                UUIDUtil.formatUUID(it.second.id)!!
                            )
                        }
                        .thenApply { _ ->
                            profileApplicationComplete(it.second)
                        }
                }
        }

    fun getApplicantProfileMapping(
        applicant: UUID
    ) = CompletableFuture
        .supplyAsync {
            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hget(
                    "disguise:disguised-players",
                    applicant.toString()
                )
        }
        .thenApplyAsync {
            if (it == null)
            {
                return@thenApplyAsync null
            }

            val uniqueId = UUID.fromString(it)

            getExistingDisguiseProfile(uniqueId)
                .join()
                ?: getExistingManualDisguiseProfile(uniqueId)
                    .join()
        }

    private fun mapApplicantToProfile(
        applicant: UUID,
        profile: UUID
    ) = CompletableFuture
        .supplyAsync {
            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hset(
                    "disguise:disguised-players",
                    applicant.toString(),
                    profile.toString()
                )
        }

    private fun removeApplicantProfileMapping(applicant: UUID) = CompletableFuture
        .runAsync {
            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hdel(
                    "disguise:disguised-players",
                    applicant.toString()
                )
        }

    fun saveDisguiseProfile(
        sessionServerResponse: SessionServerResponse
    ) = CompletableFuture
        .runAsync {
            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hset(
                    "disguise:profiles",
                    UUIDUtil
                        .formatUUID(sessionServerResponse.id)
                        .toString(),
                    Serializers.gson.toJson(sessionServerResponse)
                )
        }

    private fun setProfileNoLongerInUse(
        profile: UUID
    ) = CompletableFuture
        .supplyAsync {
            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hdel(
                    "disguise:in-use",
                    profile.toString()
                )
        }

    private fun isProfileInUse(
        profile: SessionServerResponse
    ) = CompletableFuture
        .supplyAsync {
            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hexists(
                    "disguise:in-use",
                    UUIDUtil
                        .formatUUID(profile.id)
                        .toString()
                )
        }

    fun getUsedProfile(uniqueId: UUID) = CompletableFuture
        .supplyAsync {
            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hget(
                    "disguise:in-use",
                    uniqueId.toString()
                )
        }

    private fun setDisguiseProfileInUseFor(
        player: UUID, profile: SessionServerResponse
    ) = CompletableFuture
        .supplyAsync {
            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hset(
                    "disguise:in-use",
                    UUIDUtil
                        .formatUUID(profile.id)
                        .toString(),
                    player.toString()
                )
        }

    private fun randomDisguiseProfile() = CompletableFuture
        .supplyAsync {
            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hrandfieldWithvalues(
                    "disguise:profiles"
                )
        }
        .thenApply {
            it.key to Serializers.gson
                .fromJson(
                    it.value,
                    SessionServerResponse::class.java
                )
        }

    fun disguiseProfileExists(
        uniqueId: UUID
    ) = CompletableFuture
        .supplyAsync {
            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hexists(
                    "disguise:profiles",
                    uniqueId.toString()
                )
        }

    private fun getExistingDisguiseProfile(
        uniqueId: UUID
    ) = CompletableFuture
        .supplyAsync {
            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hget(
                    "disguise:profiles",
                    uniqueId.toString()
                )
        }
        .thenApply {
            if (it == null)
                return@thenApply null

            return@thenApply Serializers.gson
                .fromJson(
                    it,
                    SessionServerResponse::class.java
                )
        }

    private fun getExistingManualDisguiseProfile(
        uniqueId: UUID
    ) = CompletableFuture
        .supplyAsync {
            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hget(
                    "disguise:manual-profiles",
                    uniqueId.toString()
                )
        }
        .thenApply {
            if (it == null)
                return@thenApply null

            return@thenApply Serializers.gson
                .fromJson(
                    it,
                    SessionServerResponse::class.java
                )
        }

    private fun cacheManualDisguiseProfile(
        uniqueId: UUID, sessionServerResponse: SessionServerResponse
    ) = CompletableFuture
        .supplyAsync {
            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hset(
                    "disguise:manual-profiles",
                    uniqueId.toString(),
                    Serializers.gson
                        .toJson(sessionServerResponse)
                )
        }

    private val runDisguiseMetadataRefresh = { player: Player ->
        PlayerHandler.findPlayer(player.uniqueId)
            .ifPresent { lemonPlayer ->
                lemonPlayer.disguiseRankUniqueId = RankHandler
                    .getDefaultRank().uuid
            }

        player
            .setMetadata(
                "disguised",
                FixedMetadataValue(plugin, true)
            )

        QuickAccess.reloadPlayer(
            player.uniqueId,
            recalculateGrants = false,
            updateVisibility = false
        )
    }

    fun undisguise(
        player: Player,
        postUnDisguise: () -> Unit = {
            player.sendMessage("${CC.GREEN}You've been undisguised!")
        }
    ) =
        runUnDisguisePipeline(
            applicant = player.uniqueId,
            applicantNotDisguised = {
                player.sendMessage("${CC.RED}You're not disguised right now!")
            },
            unDisguiseComplete = {
                Tasks.sync {
                    provider().undisguise(player)

                    PlayerHandler.findPlayer(player.uniqueId)
                        .ifPresent { lemonPlayer ->
                            lemonPlayer.disguiseRankUniqueId = null
                        }

                    player.removeMetadata(
                        "disguised",
                        plugin
                    )

                    QuickAccess.reloadPlayer(
                        player.uniqueId,
                        recalculateGrants = false,
                        updateVisibility = false
                    )

                    audiences
                        .player(player)
                        .sendActionBar(
                            Component.text()
                        )

                    postUnDisguise()
                }
            }
        )

    fun disguiseWithRandomSelection(
        player: Player,
        postDisguise: (SessionServerResponse) -> Unit = { ssr ->
            player.sendMessage(
                "${CC.SEC}You've been disguised as ${CC.PRI}${ssr.name}${CC.SEC}."
            )
        }
    ) = runDisguisePipelineWithRandomSelection(
        applicant = player.uniqueId,
        profileInUse = {
            player.sendMessage("${CC.RED}Your random disguise is already taken! Please try running this command again.")
        },
        profileApplicationComplete = { ssr ->
            completeProfileApplication(player, ssr) {
                postDisguise(ssr)
            }
        }
    )

    fun completeProfileApplication(
        player: Player, it: SessionServerResponse, success: () -> Unit
    )
    {
        val skinTextures = it.properties.first()
        val disguise = Disguise.builder()
            .setName(it.name, false)
            .setEntityType(EntityType.PLAYER)
            .setSkin(
                skinTextures.value,
                skinTextures.signature
            )
            .build()

        Tasks.sync {
            val response = DisguiseManager
                .getProvider()
                .disguise(player, disguise)

            when (response)
            {
                DisguiseResponse.SUCCESS ->
                {
                    runDisguiseMetadataRefresh(player)
                    success()
                }

                else ->
                {
                    player.sendMessage(
                        "${CC.RED}Error: Failed to disguise due to: ${
                            response.name.lowercase().replace("_", " ")
                        }"
                    )
                }
            }
        }
    }

    fun disguiseWithManualSelection(player: Player, target: UUID) =
        runDisguisePipelineWithManualSelection(
            applicant = player.uniqueId,
            disguiseTarget = target,
            profileNoResponse = {
                player.sendMessage("${CC.RED}Sorry! We cannot disguise you as this user!")
            },
            profileInUse = {
                player.sendMessage("${CC.RED}Someone on the network is already disguised as ${it.name}! Please choose another username.")
            },
            profileApplicationComplete = {
                completeProfileApplication(player, it) {
                    player.sendMessage(
                        "${CC.SEC}You've been disguised as ${CC.PRI}${it.name}${CC.GRAY}."
                    )
                }
            }
        )
}
