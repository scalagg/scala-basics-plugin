package gg.scala.basics.plugin.settings

import gg.scala.basics.plugin.settings.interfaces.DisplayEnsure
import gg.scala.basics.plugin.settings.interfaces.DisplayAttributes
import me.lucko.helper.terminable.Terminable

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
interface SettingCategory : DisplayEnsure, DisplayAttributes, Terminable
{
    val items: List<SettingEntry>

    override fun close()
    {
        SettingContainer.categories().remove(this)
    }

    override fun isClosed() =
        SettingContainer.categories().contains(this)
}
