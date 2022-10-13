package gg.scala.basics.plugin

import gg.scala.aware.AwareBuilder
import gg.scala.aware.codec.codecs.interpretation.AwareMessageCodec
import gg.scala.aware.message.AwareMessage
import gg.scala.commons.ExtendedScalaPlugin
import gg.scala.commons.annotations.container.ContainerEnable
import gg.scala.commons.core.plugin.Plugin
import gg.scala.commons.core.plugin.PluginAuthor
import gg.scala.commons.core.plugin.PluginDependency
import gg.scala.commons.core.plugin.PluginWebsite

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

@PluginDependency("scala-commons")
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
    }
}
