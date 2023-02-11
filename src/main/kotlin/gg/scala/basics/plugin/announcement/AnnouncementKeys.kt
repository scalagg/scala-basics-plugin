package gg.scala.basics.plugin.announcement

import gg.scala.commons.persist.datasync.DataSyncKeys

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
object AnnouncementKeys : DataSyncKeys
{
    override fun store() = keyOf("announcements", "configuration")
    override fun sync() = keyOf("announcements", "update")
}
