package gg.scala.basics.plugin.tablist

import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.commons.persist.datasync.DataSyncService
import gg.scala.commons.persist.datasync.DataSyncSource
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import me.lucko.helper.Events
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.event.player.PlayerJoinEvent

/**
 * @author GrowlyX
 * @since 10/16/2022
 */
@Service
object TabListService : DataSyncService<TabListConfigModel>()
{
    @Inject
    lateinit var plugin: ScalaBasicsPlugin

    @Inject
    lateinit var audiences: BukkitAudiences

    override fun locatedIn() = DataSyncSource.Mongo

    @Configure
    fun configure()
    {
        Events
            .subscribe(PlayerJoinEvent::class.java)
            .handler {
                val audience = audiences.player(it.player)
                audience.sendPlayerListHeaderAndFooter(
                    cached().header, cached().footer
                )
            }
            .bindWith(plugin)
    }

    override fun keys() = TabListKeys
    override fun type() = TabListConfigModel::class.java
}
