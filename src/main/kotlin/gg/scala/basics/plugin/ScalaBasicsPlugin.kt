package gg.scala.basics.plugin

import gg.scala.aware.AwareBuilder
import gg.scala.aware.codec.codecs.interpretation.AwareMessageCodec
import gg.scala.aware.message.AwareMessage
import gg.scala.basics.plugin.settings.SettingCategory
import gg.scala.basics.plugin.settings.SettingContainer
import gg.scala.commons.ExtendedScalaPlugin
import gg.scala.commons.annotations.container.ContainerEnable
import gg.scala.commons.core.plugin.Plugin
import gg.scala.commons.core.plugin.PluginAuthor
import gg.scala.commons.core.plugin.PluginDependency
import gg.scala.commons.core.plugin.PluginWebsite
import gg.scala.commons.preconfigure.PreConfigureSubTypeProcessor

/**
 * @author GrowlyX
 * @since 7/24/2022
 */
@Plugin(
    name = "ScBasics",
    version = "%remote%/%branch%/%id%"
)
@PluginAuthor("Scala")
@PluginWebsite("https://scala.gg")

@PluginDependency("Lemon")
@PluginDependency("scala-commons")
@PluginDependency("cloudsync", soft = true)
class ScalaBasicsPlugin : ExtendedScalaPlugin()
{
    val aware by lazy {
        AwareBuilder
            .of<AwareMessage>("basics")
            .codec(AwareMessageCodec)
            .logger(logger)
            .build()
    }

    @ContainerEnable
    fun containerEnable()
    {
        aware.connect().toCompletableFuture().join()

        PreConfigureSubTypeProcessor
            .register<SettingCategory> {
                SettingContainer.categories().add(it)

                logger.info(
                    "[Player Settings] Registered settings factory ${it.javaClass.name}."
                )
            }
    }
}
