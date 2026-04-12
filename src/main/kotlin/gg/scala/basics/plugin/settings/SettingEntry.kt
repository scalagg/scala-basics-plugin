package gg.scala.basics.plugin.settings

import gg.scala.basics.plugin.settings.interfaces.DisplayEnsure
import gg.scala.basics.plugin.settings.interfaces.DisplayAttributes
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.entity.Player

interface SettingEntry : DisplayEnsure, DisplayAttributes
{
    val id: String
    val valueType: Class<out SettingValue>
    val item: ItemBuilder
    val default: SettingValue

    val postChange: (Player) -> Unit

    val customLogic: Boolean
    val customLogicLambda: (Player) -> Unit
}
