package gg.scala.basics.plugin.settings

import gg.scala.basics.plugin.settings.interfaces.DisplayPredicate
import gg.scala.basics.plugin.settings.interfaces.Displayable
import net.evilblock.cubed.util.bukkit.ItemBuilder

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
interface SettingEntry : DisplayPredicate, Displayable
{
    val id: String
    val valueType: Class<SettingValue<*>>
    val item: ItemBuilder
}
