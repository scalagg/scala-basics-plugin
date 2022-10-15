package gg.scala.basics.plugin.settings.defaults

import gg.scala.basics.plugin.conversation.settings.MessagingSettingValue
import gg.scala.basics.plugin.profile.BasicsProfileService
import gg.scala.basics.plugin.settings.SettingCategory
import gg.scala.basics.plugin.settings.defaults.values.StateSettingValue
import gg.scala.basics.plugin.settings.entry
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.lemon.listener.PlayerListener
import net.evilblock.cubed.util.CC
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
            displayName = "Direct messages"

            clazz = MessagingSettingValue::class.java
            default = MessagingSettingValue.FRIENDS_ONLY

            description += "Allows you to disable"
            description += "direct messages through"
            description += "/message."
            description += ""
            description += "This setting is set to"
            description += "${CC.BOLD}Friends Only${CC.WHITE} by default"
            description += "for privacy purposes."

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
            displayName = "Server announcements"

            clazz = StateSettingValue::class.java
            default = StateSettingValue.ENABLED

            description += "Allows you to disable"
            description += "periodical server"
            description += "announcements."

            item = ItemBuilder.of(Material.ARROW)
        },
        entry {
            id = "global_chat"
            displayName = "Global chat"

            clazz = StateSettingValue::class.java
            default = StateSettingValue.ENABLED

            description += "Allows you to hide"
            description += "messages sent through"
            description += "the server's global chat."

            item = ItemBuilder.of(Material.SIGN)
        }
    )

    @Configure
    fun configure()
    {
        PlayerListener.defaultChannelProtection2 = ctx@{
            val profile = BasicsProfileService
                .find(it.player)
                ?: return@ctx

            val globalChat = profile.setting(
                id = "global_chat",
                default = StateSettingValue.ENABLED
            )

            if (globalChat == StateSettingValue.DISABLED)
            {
                it.isCancelled = true
                it.player.sendMessage("${CC.RED}You have global chat disabled, please re-enable it to continue talking.")
                return@ctx
            }
        }

        PlayerListener.defaultChannelProtection3 = ctx@{
            val profile = BasicsProfileService
                .find(it) ?: return@ctx true

            val globalChat = profile.setting(
                id = "global_chat",
                default = StateSettingValue.ENABLED
            )

            if (globalChat == StateSettingValue.DISABLED)
            {
                return@ctx false
            }

            true
        }
    }

    override fun display(player: Player) = true

    override val displayName = "General"
    override val description = listOf(
        "Network-wide, customizable",
        "options."
    )
}
