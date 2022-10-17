package gg.scala.basics.plugin.announcement

import java.util.concurrent.TimeUnit

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
data class AnnouncementConfigModel(
    val announcements: MutableList<Announcement> =
        mutableListOf(
            Announcement(mutableListOf("Hello world, from basics."))
        ),
    var padding: Boolean = false,
    var linePrefix: String = "",
    var iterationSpeed: Long = 1L,
    var iterationTimeUnit: TimeUnit = TimeUnit.MINUTES
)
