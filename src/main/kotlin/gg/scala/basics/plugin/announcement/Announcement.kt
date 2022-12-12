package gg.scala.basics.plugin.announcement

import net.kyori.adventure.key.Key

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
data class Announcement(
    val lines: MutableList<String>,
    var conditions: MutableList<Key>? = null
)
