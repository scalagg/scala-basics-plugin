package gg.scala.basics.plugin.announcement.keys

import org.bukkit.ChatColor

enum class LogicCondition(
    val data: Short,
    val displayName: String,
    val color: ChatColor
)
{
    And(5, "AND", ChatColor.GREEN),
    Not(14, "NOT", ChatColor.RED),
    Xor(1, "XOR", ChatColor.GOLD),
    Or(11, "OR", ChatColor.BLUE)
}