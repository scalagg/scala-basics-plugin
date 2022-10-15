package gg.scala.basics.plugin.settings.defaults

import gg.scala.basics.plugin.settings.SettingCategory
import gg.scala.basics.plugin.settings.defaults.values.StateSettingValue
import gg.scala.basics.plugin.settings.entry
import gg.scala.basics.plugin.conversation.settings.MessagingSettingValue
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
            id = "messages"
            displayName = "Private Messages"

            clazz = MessagingSettingValue::class.java
            default = MessagingSettingValue.ENABLED

            description += "Allows you to disable"
            description += "private messages through"
            description += "/message."

            item = ItemBuilder.of(Material.BOOK)
        },
        entry {
            id = "messages_sounds"
            displayName = "Message sounds"

            clazz = StateSettingValue::class.java
            default = StateSettingValue.ENABLED

            description += "Allows you to disable the pling"
            description += "sound that is played when you"
            description += "receive a private message."

            item = ItemBuilder.of(Material.JUKEBOX)
        },
        entry {
            id = "announcements"
            displayName = "Announcement Stream"

            clazz = StateSettingValue::class.java
            default = StateSettingValue.ENABLED

            description += "Allows you to disable"
            description += "periodical server"
            description += "announcements."

            item = ItemBuilder.of(Material.FIREBALL)
        }
    )

    override fun display(player: Player) = true

    override val displayName = "General"
    override val description = listOf(
        "Network-wide, customizable",
        "options."
    )
}
