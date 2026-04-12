package gg.scala.basics.plugin.cache

import net.evilblock.cubed.ScalaCommonsSpigot

object SharedAwareService
{
    fun cache() = ScalaCommonsSpigot.instance.kvConnection
}
