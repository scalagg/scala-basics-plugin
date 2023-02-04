package gg.scala.basics.plugin.chatfilter

import net.kyori.adventure.key.Key

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
object ChatFilterKeys
{
    private const val NAMESPACE = "ChatFilter"

    val updateKey = Key
        .key(NAMESPACE, "update")
        .toString()

    val storeKey = Key
        .key(NAMESPACE, "configuration")
        .toString()
}
