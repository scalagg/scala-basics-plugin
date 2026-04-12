package gg.scala.basics.plugin.announcement.conditions

import gg.scala.basics.plugin.announcement.keys.AnnouncementKey
import net.kyori.adventure.key.Key
import org.bukkit.entity.Player

interface AnnouncementDisplayCondition
{
    fun contextualKey(): Boolean = false
    fun contextualKeyNamespace(): String = ""
    fun contextualDisplay(player: Player, key: AnnouncementKey): Boolean = false

    fun key(): AnnouncementKey
    fun shouldDisplay(player: Player): Boolean
}
