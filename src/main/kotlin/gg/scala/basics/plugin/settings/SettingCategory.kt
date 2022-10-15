package gg.scala.basics.plugin.settings

import gg.scala.basics.plugin.settings.interfaces.DisplayEnsure
import gg.scala.basics.plugin.settings.interfaces.DisplayAttributes

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
interface SettingCategory : DisplayEnsure, DisplayAttributes
{
    val items: List<SettingEntry>
}
