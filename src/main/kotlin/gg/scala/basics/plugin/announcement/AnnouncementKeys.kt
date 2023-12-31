package gg.scala.basics.plugin.announcement

import gg.scala.commons.agnostic.sync.ServerSync
import gg.scala.commons.persist.datasync.DataSyncKeys

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
object AnnouncementKeys : DataSyncKeys
{
    override fun store() = keyOf("announcements", "configuration${if (ServerSync.getLocalGameServer().groups.contains("dev")) "-dev" else ""}")

    override fun newStore() = "announcements"
    override fun sync() = keyOf("announcements", "configuration${if (ServerSync.getLocalGameServer().groups.contains("dev")) "-dev" else ""}")
}
