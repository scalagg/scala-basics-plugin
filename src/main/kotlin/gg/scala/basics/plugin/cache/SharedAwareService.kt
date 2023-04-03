package gg.scala.basics.plugin.cache

import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import net.evilblock.cubed.ScalaCommonsSpigot

/**
 * @author GrowlyX
 * @since 2/4/2023
 */
object SharedAwareService
{
    fun cache() = ScalaCommonsSpigot.instance.kvConnection
}
