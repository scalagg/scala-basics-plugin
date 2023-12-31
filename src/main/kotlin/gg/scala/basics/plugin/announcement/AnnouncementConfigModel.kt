package gg.scala.basics.plugin.announcement

import java.util.concurrent.TimeUnit

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
data class AnnouncementConfigModel(
    val announcements: MutableMap<Int, Announcement> =
        mutableMapOf(0 to Announcement(0, mutableListOf("Hello world, from Basics!"))),
    var padding: Boolean = false,
    var linePrefix: String = "",
    var iterationSpeed: Long = 1L,
    var iterationTimeUnit: TimeUnit = TimeUnit.MINUTES
)
