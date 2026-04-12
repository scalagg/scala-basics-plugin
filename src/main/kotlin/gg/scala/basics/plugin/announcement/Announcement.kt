package gg.scala.basics.plugin.announcement

import gg.scala.basics.plugin.announcement.keys.AnnouncementKey
import net.kyori.adventure.key.Key

data class Announcement(
    val id: Int,
    val lines: MutableList<String>,
    val conditions: MutableMap<String, AnnouncementKey> = mutableMapOf()
)
