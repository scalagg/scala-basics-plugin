package gg.scala.basics.plugin.service

import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import me.lucko.helper.Events
import me.lucko.helper.metadata.Metadata
import me.lucko.helper.metadata.MetadataKey
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * @author GrowlyX
 * @since 10/13/2022
 */
@Service
object LoginTimeService
{
    @Inject
    lateinit var plugin: ScalaBasicsPlugin

    private val loginTime = MetadataKey
        .createLongKey("login-time")

    fun loginTime(player: Player) = Metadata
        .provideForPlayer(player)
        .get(loginTime)
        .orElseGet {
            System.currentTimeMillis()
        }!!

    @Configure
    fun configure()
    {
        Events.subscribe(PlayerJoinEvent::class.java)
            .handler {
                Metadata
                    .provideForPlayer(it.player)
                    .put(
                        loginTime, System.currentTimeMillis()
                    )
            }
            .bindWith(plugin)

        Events.subscribe(PlayerQuitEvent::class.java)
            .handler {
                Metadata
                    .provideForPlayer(it.player)
                    .remove(loginTime)
            }
            .bindWith(plugin)
    }
}
