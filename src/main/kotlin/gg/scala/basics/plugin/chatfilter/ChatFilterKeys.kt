package gg.scala.basics.plugin.chatfilter

import gg.scala.commons.persist.datasync.DataSyncKeys

object ChatFilterKeys : DataSyncKeys
{
    private const val NAMESPACE = "chatfilter"

    override fun store() = keyOf(NAMESPACE, "configuration")
    override fun newStore() = NAMESPACE
    override fun sync() = keyOf(NAMESPACE, "update")
}
