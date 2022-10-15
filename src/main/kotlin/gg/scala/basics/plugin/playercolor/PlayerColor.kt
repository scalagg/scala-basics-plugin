package gg.scala.basics.plugin.playercolor

import org.bukkit.ChatColor
import org.bukkit.Color

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
data class PlayerColor(
    val chatColor: ChatColor,
    val bukkitColor: Color,
    val name: String
)
