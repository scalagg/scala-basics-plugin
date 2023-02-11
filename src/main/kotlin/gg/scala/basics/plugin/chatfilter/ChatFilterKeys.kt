package gg.scala.basics.plugin.chatfilter

import gg.scala.commons.persist.datasync.DataSyncKeys

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
object ChatFilterKeys : DataSyncKeys
{
    private const val NAMESPACE = "chatfilter"

    override fun store() = keyOf(NAMESPACE, "configuration")
    override fun sync() = keyOf(NAMESPACE, "update")
}
