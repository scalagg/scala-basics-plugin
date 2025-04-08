package gg.scala.basics.plugin.tablist

import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.basics.plugin.tablist.animation.TabAnimationService
import gg.scala.commons.agnostic.sync.server.ServerContainer
import gg.scala.commons.agnostic.sync.server.impl.GameServer
import gg.scala.commons.scoping.selectScoped
import gg.scala.commons.persist.datasync.DataSyncService
import gg.scala.commons.persist.datasync.DataSyncSource
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import me.lucko.helper.Events
import me.lucko.helper.Schedulers
import me.lucko.helper.utils.Players
import net.evilblock.cubed.ScalaCommonsSpigot
import net.evilblock.cubed.util.math.Numbers
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent

/**
 * @author GrowlyX
 * @since 10/16/2022
 */
@Service
object TabListService : DataSyncService<TabListConfig>()
{
    @Inject
    lateinit var plugin: ScalaBasicsPlugin

    @Inject
    lateinit var audiences: BukkitAudiences

    override fun locatedIn() = DataSyncSource.Mongo

    private var globalPlayerCount = 0
    private var mipWaiting = 0
    private var mipPlaying = 0
    val gameServers = ServerContainer
        .getServersInGroupCasted<GameServer>("mipgame")

    fun localConfig() = cached().configs().selectScoped()

    @Configure
    fun configure()
    {
        Events
            .subscribe(PlayerJoinEvent::class.java)
            .handler {
                updateFor(it.player)
            }
            .bindWith(plugin)

        Schedulers
            .async()
            .runRepeating(Runnable {
                globalPlayerCount = ScalaCommonsSpigot
                    .instance.kvConnection
                    .sync().get(
                        "global-player-count"
                    )
                    ?.toIntOrNull() ?: 0
                mipWaiting = ScalaCommonsSpigot
                    .instance.kvConnection
                    .sync().hlen(
                        "scalapractice:queue-states"
                    )
                    .toInt()
                mipPlaying = gameServers.sumOf { it.getPlayersCount() ?: 0 }
            }, 0L, 1L)

        Schedulers.async()
            .runRepeating(Runnable {
                val local = localConfig()
                    ?: return@Runnable

                if (local.shouldAutoUpdate)
                {
                    updateForAll()
                }
            }, 0L, 1L)
    }

    override fun postReload()
    {
        updateForAll()
    }

    fun updateForAll()
    {
        Players.all().forEach { player ->
            updateFor(player)
        }
    }

    fun updateFor(player: Player)
    {
        val local = localConfig()
            ?: return

        val audience = audiences.player(player)
        val formattedHeader = LegacyComponentSerializer.legacySection().deserialize(
            local.header.joinToString("\n") {
                it
                    .replace("{playerCount}", Numbers.format(globalPlayerCount))
                    .replace("{animatedServerName}", TabAnimationService.getCurrentTitle())
                    .replace("{mipWaiting}", Numbers.format(mipWaiting))
                    .replace("{mipPlaying}", Numbers.format(mipPlaying))
            }
        )

        val formattedFooter = LegacyComponentSerializer.legacySection().deserialize(
            local.footer.joinToString("\n") {
                it.replace("{playerCount}", Numbers.format(globalPlayerCount))
            }
        )

        audience.sendPlayerListHeaderAndFooter(formattedHeader, formattedFooter)
    }

    override fun keys() = TabListKeys
    override fun type() = TabListConfig::class.java
}
