package gg.scala.basics.plugin.settings.defaults.values

import gg.scala.basics.plugin.settings.SettingValue
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
enum class TimeOfDaySettingValue : SettingValue
{
    Day, Sunset, Night, Local;

    override val displayName = name
    override fun display(player: Player) = true
}
