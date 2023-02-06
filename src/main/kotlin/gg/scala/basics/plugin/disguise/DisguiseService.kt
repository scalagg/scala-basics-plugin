package gg.scala.basics.plugin.disguise

import dev.iiahmed.disguise.DisguiseManager
import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service

/**
 * @author GrowlyX
 * @since 2/5/2023
 */
@Service
object DisguiseService
{
    @Inject
    lateinit var plugin: ScalaBasicsPlugin

    private val provider by lazy {
        DisguiseManager.getProvider()
    }

    fun provider() = provider

    @Configure
    fun configure()
    {
        DisguiseManager.setPlugin(plugin)
        provider
    }
}
