package gg.scala.basics.plugin.cache

import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service

/**
 * @author GrowlyX
 * @since 2/4/2023
 */
@Service(priority = 300)
object SharedAwareService
{
    @Inject
    lateinit var plugin: ScalaBasicsPlugin

    private val cache by lazy {
        plugin.aware.internal().connect()
    }

    fun cache() = cache

    @Configure
    fun configure()
    {
        cache
    }
}
