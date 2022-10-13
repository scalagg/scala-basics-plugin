package gg.scala.basics.plugin.announcement

import net.kyori.adventure.key.Key

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
object AnnouncementKeys
{
    val updateKey = Key
        .key("announcements", "update")
        .toString()

    val storeKey = Key
        .key("announcements", "configuration")
        .toString()
}
