package gg.scala.basics.plugin.settings

import gg.scala.basics.plugin.settings.interfaces.DisplayPredicate
import gg.scala.basics.plugin.settings.interfaces.Displayable

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
interface SettingCategory : DisplayPredicate, Displayable
{
    val items: List<SettingEntry>
}
