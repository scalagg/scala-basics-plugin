package gg.scala.basics.plugin.cache

import net.evilblock.cubed.ScalaCommonsSpigot

/**
 * @author GrowlyX
 * @since 2/4/2023
 */
object SharedAwareService
{
    fun cache() = ScalaCommonsSpigot.instance.kvConnection
}
