package gg.scala.basics.plugin.commands.player

import gg.scala.basics.plugin.conversation.settings.MessagingSettingValue
import gg.scala.basics.plugin.profile.BasicsProfileService
import gg.scala.basics.plugin.settings.SettingMenu
import gg.scala.basics.plugin.settings.defaults.values.StateSettingValue
import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import net.evilblock.cubed.util.CC

/**
 * @author GrowlyX
 * @since 10/13/2022
 */
@AutoRegister
object SettingsCommand : ScalaCommand()
{
    @CommandPermission("scbasics.player-commands")
    @CommandAlias("settings|prefs|preferences|mysettings|myprefs")
    fun onPing(player: ScalaPlayer)
    {
        SettingMenu(player.bukkit()).openMenu(player.bukkit())
    }

    @CommandPermission("scbasics.player-commands")
    @CommandAlias(
        "togglemessagesounds|tms|togglesounds|ts"
    )
    fun onSounds(player: ScalaPlayer)
    {
        val profile = BasicsProfileService.find(player.bukkit())
            ?: throw ConditionFailedException(
                "Sorry, your profile did not load properly."
            )

        val messagesRef = profile.settings["messages_sounds"]!!
        val mapped = messagesRef.map<StateSettingValue>()

        if (mapped == StateSettingValue.ENABLED)
        {
            messagesRef.value = "DISABLED"

            player.sendMessage(
                "${CC.RED}You've disabled message sounds."
            )
        } else
        {
            messagesRef.value = "ENABLED"

            player.sendMessage(
                "${CC.GREEN}You've enabled message sounds."
            )
        }

        profile.save()
    }

    @CommandPermission("scbasics.player-commands")
    @CommandAlias(
        "toggleannouncements|ta"
    )
    fun onAnnouncements(player: ScalaPlayer)
    {
        val profile = BasicsProfileService.find(player.bukkit())
            ?: throw ConditionFailedException(
                "Sorry, your profile did not load properly."
            )

        val messagesRef = profile.settings["announcements"]!!
        val mapped = messagesRef.map<StateSettingValue>()

        if (mapped == StateSettingValue.ENABLED)
        {
            messagesRef.value = "DISABLED"

            player.sendMessage(
                "${CC.RED}You've disabled announcements."
            )
        } else
        {
            messagesRef.value = "ENABLED"

            player.sendMessage(
                "${CC.GREEN}You've enabled announcements."
            )
        }

        profile.save()
    }

    @CommandPermission("scbasics.player-commands")
    @CommandAlias(
        "tgc|globalchat|toggleglobalchat"
    )
    fun onGlobalChat(player: ScalaPlayer)
    {
        val profile = BasicsProfileService.find(player.bukkit())
            ?: throw ConditionFailedException(
                "Sorry, your profile did not load properly."
            )

        val messagesRef = profile.settings["global_chat"]!!
        val mapped = messagesRef.map<StateSettingValue>()

        if (mapped == StateSettingValue.ENABLED)
        {
            messagesRef.value = "DISABLED"

            player.sendMessage(
                "${CC.RED}You've disabled global chat."
            )
        } else
        {
            messagesRef.value = "ENABLED"

            player.sendMessage(
                "${CC.GREEN}You've enabled global chat."
            )
        }

        profile.save()
    }

    @CommandPermission("scbasics.player-commands")
    @CommandAlias(
        "toggleprivatemessages|toggledirectmessages|tpm|tdm|dms|pms|directmessages|privatemessages"
    )
    fun onDirectMessages(player: ScalaPlayer)
    {
        val profile = BasicsProfileService.find(player.bukkit())
            ?: throw ConditionFailedException(
                "Sorry, your profile did not load properly."
            )

        val messageSettingValue = profile
            .setting(
                id = "messages",
                default = MessagingSettingValue.FRIENDS_ONLY
            )

        val messagesRef = profile.settings["messages"]!!

        when (messageSettingValue)
        {
            MessagingSettingValue.ENABLED -> messagesRef.value = "FRIENDS_ONLY"
            MessagingSettingValue.FRIENDS_ONLY -> messagesRef.value = "DISABLED"
            MessagingSettingValue.DISABLED -> messagesRef.value = "ENABLED"
        }

        profile.save()

        player.sendMessage("${CC.GREEN}Your private messages are now set to ${CC.YELLOW}${
            profile
                .setting(
                    id = "messages",
                    default = MessagingSettingValue.FRIENDS_ONLY
                )
                .displayName
        }${CC.GREEN}.")
    }
}
