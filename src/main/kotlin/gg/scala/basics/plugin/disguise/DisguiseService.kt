package gg.scala.basics.plugin.disguise

import dev.iiahmed.disguise.Disguise
import dev.iiahmed.disguise.DisguiseManager
import dev.iiahmed.disguise.DisguiseResponse
import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.basics.plugin.commands.disguise.DisguiseCommands
import gg.scala.basics.plugin.disguise.sessionserver.SessionServerResponse
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.lemon.handler.PlayerHandler
import gg.scala.lemon.handler.RankHandler
import gg.scala.lemon.util.QuickAccess
import net.evilblock.cubed.ScalaCommonsSpigot
import net.evilblock.cubed.serializers.Serializers
import net.evilblock.cubed.util.CC
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
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

    private val provider by lazy {
        DisguiseManager.getProvider()
    }

    fun provider() = provider

    @Configure
    fun configure()
    {
        DisguiseManager.setPlugin(plugin)
        provider
    }

    fun saveDisguiseProfile(
        sessionServerResponse: SessionServerResponse
    ) = CompletableFuture
        .runAsync {
            ScalaCommonsSpigot.instance.kvConnection
                .sync()
                .hset(
                    "disguise:profiles",
                    sessionServerResponse.id.toString(),
                    Serializers.gson.toJson(sessionServerResponse)
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

    fun disguise(player: Player, name: String)
    {
        val disguise = Disguise.builder()
            .setName(name, true)
            .setEntityType(EntityType.PLAYER)
            .build()

        val response = provider()
            .disguise(player, disguise)

        when (response)
        {
            DisguiseResponse.SUCCESS ->
            {
                player.sendMessage(
                    "${CC.SEC}You've disguised as ${CC.PRI}$name${CC.SEC}. ${CC.GRAY}(with a random skin)"
                )

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
                    player.uniqueId, false
                )
            }
            DisguiseResponse.FAIL_NAME_ALREADY_ONLINE -> player.sendMessage("${CC.RED}Someone with that username is already online!")
            else -> player.sendMessage("${CC.RED}You're unable to disguise right now!")
        }
    }
}
