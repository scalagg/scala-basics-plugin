package gg.scala.basics.plugin.tablist

import gg.scala.commons.persist.datasync.DataSyncKeys
import net.kyori.adventure.key.Key

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
object TabListKeys : DataSyncKeys
{
    override fun store() = keyOf("tablist", "configuration")
    override fun sync() = keyOf("tablist", "update")
}
