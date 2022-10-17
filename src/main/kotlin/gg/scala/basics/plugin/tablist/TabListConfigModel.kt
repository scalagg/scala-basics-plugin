package gg.scala.basics.plugin.tablist

import net.kyori.adventure.text.Component

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
data class TabListConfigModel(
    var header: Component = Component.empty(),
    var footer: Component = Component.empty()
)
