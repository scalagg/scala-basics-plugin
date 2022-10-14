package gg.scala.basics.plugin.settings.defaults

import gg.scala.basics.plugin.settings.SettingCategory
import gg.scala.basics.plugin.settings.StateSettingValue
import gg.scala.basics.plugin.settings.entry
import gg.scala.flavor.service.Service
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
@Service
object DefaultSettingsCategory : SettingCategory
{
    override val items = listOf(
        entry {
            id = "announcements"
            displayName = "Announcement Stream"

            clazz = StateSettingValue::class.java
            default = StateSettingValue.ENABLED

            description += "This setting toggles"
            description += "announcement receiving on all"
            description += "of our servers."

            item = ItemBuilder.of(Material.FIREBALL)
        }
    )

    override fun display(player: Player) = true

    override val displayName = "General"
    override val description = listOf("Network-wide, customizable options.")
}
