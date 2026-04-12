package gg.scala.basics.plugin.announcement

import gg.scala.commons.agnostic.sync.ServerSync
import gg.scala.commons.persist.datasync.DataSyncKeys

object AnnouncementKeys : DataSyncKeys
{
    private fun getDevServerSuffix() = if (ServerSync.getLocalGameServer().groups.contains("dev")) "-dev" else ""
    override fun store() = keyOf("announcements", "configuration${getDevServerSuffix()}")

    override fun newStore() = "announcements${getDevServerSuffix()}"
    override fun sync() = keyOf("announcements", "configuration${getDevServerSuffix()}")
}
