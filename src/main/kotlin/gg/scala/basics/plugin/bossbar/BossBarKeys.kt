package gg.scala.basics.plugin.bossbar

import gg.scala.commons.persist.datasync.DataSyncKeys

object BossBarKeys : DataSyncKeys
{
    override fun store() = keyOf("bossbar", "configuration")

    override fun newStore() = "bossbar"
    override fun sync() = keyOf("bossbar", "update")
}
