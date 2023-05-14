package gg.scala.basics.plugin.settings.defaults

import gg.scala.basics.plugin.conversation.settings.MessagingSettingValue
import gg.scala.basics.plugin.profile.BasicsProfileService
import gg.scala.basics.plugin.settings.SettingCategory
import gg.scala.basics.plugin.settings.defaults.values.entity.EntityVisibilitySettingValue
import gg.scala.basics.plugin.settings.defaults.values.StateSettingValue
import gg.scala.basics.plugin.settings.defaults.values.TimeOfDaySettingValue
import gg.scala.basics.plugin.settings.entry
import gg.scala.basics.plugin.timeofday.TimeOfDayService
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
            default = MessagingSettingValue.ENABLED

            description += "Allows you to disable"
            description += "direct messages through"
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
        },
        entry {
            id = "time_of_day"
            displayName = "Time of day"

            clazz = TimeOfDaySettingValue::class.java
            default = TimeOfDaySettingValue.Day

            description += "Allows you to change"
            description += "the time of day in-game."

            postChange = { player ->
                TimeOfDayService.toggleToDFromProfile(player)
            }

            item = ItemBuilder.of(Material.WATCH)
        },
        entry {
            id = "auto_disguise"
            displayName = "Auto Disguise"

            clazz = StateSettingValue::class.java
            default = StateSettingValue.ENABLED

            description += "Allows you to disguise"
            description += "yourself automatically"
            description += "when you join the network."

            displayPredicate = {
                it.hasPermission("scbasics.disguise.automatic")
            }

            item = ItemBuilder
                .of(Material.POTION)
                .data(8270)
        },
        entry {
            id = "entity_render_distance"
            displayName = "Entity render distance"

            clazz = EntityVisibilitySettingValue::class.java
            default = EntityVisibilitySettingValue.High

            description += "Represents the distance"
            description += "requirement you have to"
            description += "meet before NPC and hologram"
            description += "entities are loaded in."

            postChange = { player ->
                player.sendMessage(
                    "${CC.GRAY}[Note] ${CC.GREEN}You may have to re-log for your changes to be put into effect!"
                )
            }

            item = ItemBuilder.of(Material.MOB_SPAWNER)
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
