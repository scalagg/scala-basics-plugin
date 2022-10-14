package gg.scala.basics.plugin.settings.defaults

import gg.scala.basics.plugin.settings.SettingCategory
import gg.scala.basics.plugin.settings.SettingEntry
import gg.scala.basics.plugin.settings.entry
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
object DefaultSettingsCategory : SettingCategory
{
    override val items = listOf<SettingEntry>(
        entry {  }
    )

    override fun display(player: Player) = true

    override val displayName = "General"
    override val description = listOf("Network-wide, customizable options.")
}
