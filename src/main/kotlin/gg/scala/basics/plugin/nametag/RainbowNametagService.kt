package gg.scala.basics.plugin.nametag

import gg.scala.commons.annotations.runnables.Repeating
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import me.lucko.helper.Events
import net.evilblock.cubed.nametag.NametagHandler
import net.evilblock.cubed.nametag.NametagProvider
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

/**
 * @author GrowlyX
 * @since 10/13/2022
 */
@Service
@Repeating(20L)
object RainbowNametagService : Runnable
{
    var index = 0

    val options = listOf(
        ChatColor.DARK_RED,
        ChatColor.RED,
        ChatColor.GOLD,
        ChatColor.YELLOW,
        ChatColor.GREEN,
        ChatColor.DARK_GREEN,
        ChatColor.AQUA,
        ChatColor.DARK_AQUA,
        ChatColor.BLUE,
        ChatColor.DARK_BLUE,
        ChatColor.DARK_PURPLE,
        ChatColor.LIGHT_PURPLE
    )

    var currentColor = options.first()

    override fun run()
    {
        index++

        if (index >= options.size)
        {
            index = 0
        }

        currentColor = options[index]

        nametagInfo = NametagProvider.createNametag(
            currentColor.toString(), ""
        )

        rainbowNametagEnabled.forEach {
            val bukkitPlayer = Bukkit.getPlayer(it)
                ?: return@forEach

            NametagHandler.reloadPlayer(bukkitPlayer)
        }
    }

    val rainbowNametagEnabled = mutableSetOf<UUID>()

    var nametagInfo = NametagProvider.createNametag(
        currentColor.toString(), ""
    )

    @Configure
    fun configure()
    {
        Events.subscribe(PlayerQuitEvent::class.java)
            .handler {
                rainbowNametagEnabled.remove(it.player.uniqueId)
            }
    }
}
