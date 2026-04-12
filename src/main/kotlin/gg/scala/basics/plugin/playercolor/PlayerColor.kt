package gg.scala.basics.plugin.playercolor

import org.bukkit.ChatColor
import org.bukkit.Color

data class PlayerColor(
    val chatColor: ChatColor,
    val bukkitColor: Color,
    val name: String
)
