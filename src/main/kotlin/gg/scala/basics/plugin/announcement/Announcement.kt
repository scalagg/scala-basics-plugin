package gg.scala.basics.plugin.announcement

import gg.scala.basics.plugin.announcement.keys.AnnouncementKey
import net.kyori.adventure.key.Key

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
data class Announcement(
    val id: Int,
    val lines: MutableList<String>,
    var targetServerGroup: String? = null,
    val conditions: MutableMap<String, AnnouncementKey> = mutableMapOf()
)
