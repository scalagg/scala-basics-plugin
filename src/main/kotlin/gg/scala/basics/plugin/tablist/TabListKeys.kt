package gg.scala.basics.plugin.tablist

import net.kyori.adventure.key.Key

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
object TabListKeys
{
    val updateKey = Key
        .key("tablist", "update")
        .toString()

    val storeKey = Key
        .key("tablist", "configuration")
        .toString()
}
