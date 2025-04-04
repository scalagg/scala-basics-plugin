package gg.scala.basics.plugin.bossbar

import gg.scala.commons.persist.datasync.DataSyncKeys

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
object BossBarKeys : DataSyncKeys
{
    override fun store() = keyOf("bossbar", "configuration")

    override fun newStore() = "bossbar"
    override fun sync() = keyOf("bossbar", "update")
}
