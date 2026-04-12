package gg.scala.basics.plugin.tablist

import gg.scala.commons.persist.datasync.DataSyncKeys
import net.kyori.adventure.key.Key

object TabListKeys : DataSyncKeys
{
    @Deprecated("newStore()")
    override fun store() = keyOf("tablist", "configuration")

    override fun newStore() = "tablist-v3"
    override fun sync() = keyOf("tablist", "update")
}
