package gg.scala.basics.plugin.conversation

import com.cryptomorin.xseries.XSound
import gg.scala.basics.plugin.conversation.message.PrivateConversationMessage
import gg.scala.basics.plugin.conversation.message.PrivateConversationMessageResponse
import gg.scala.basics.plugin.conversation.message.PrivateConversationMessageResponseContext
import gg.scala.basics.plugin.conversation.settings.MessagingSettingValue
import gg.scala.basics.plugin.profile.BasicsProfileService
import gg.scala.basics.plugin.settings.defaults.values.StateSettingValue
import gg.scala.friends.FriendsSpigotPlugin
import gg.scala.friends.handler.KPlayerHandler
import gg.scala.lemon.handler.PlayerHandler
import gg.scala.lemon.player.metadata.Metadata
import net.evilblock.cubed.util.CC
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author GrowlyX
 * @since 3/11/2022
 */
object PrivateConversationMessageResponder : (PrivateConversationMessage) -> PrivateConversationMessageResponse
{
    override fun invoke(
        message: PrivateConversationMessage
    ): PrivateConversationMessageResponse
    {
        val bukkitPlayer = Bukkit
            .getPlayer(message.targetedReceiver)
            ?: return PrivateConversationMessageResponse(
                message.uniqueId, null,
                PrivateConversationMessageResponseContext.TARGET_NOT_ONLINE
            )

        val lemonPlayer = PlayerHandler
            .findPlayer(message.targetedReceiver)
            .orElse(null)
            ?: return PrivateConversationMessageResponse(
                message.uniqueId, null,
                PrivateConversationMessageResponseContext.TARGET_NOT_ONLINE
            )

        val profile = BasicsProfileService
            .find(bukkitPlayer)
            ?: return PrivateConversationMessageResponse(
                message.uniqueId, null,
                PrivateConversationMessageResponseContext.TARGET_NOT_ONLINE
            )

        val messagingSettingValue = profile
            .setting(
                id = "messages",
                default = MessagingSettingValue.ENABLED
            )

        if (Bukkit.getPluginManager().isPluginEnabled("Friends"))
        {
            val friendsPlayer = KPlayerHandler.find(bukkitPlayer)
                ?: return PrivateConversationMessageResponse(
                    message.uniqueId, null,
                    PrivateConversationMessageResponseContext.TARGET_NOT_ONLINE
                )

            if (
                messagingSettingValue == MessagingSettingValue.FRIENDS_ONLY &&
                !message.dispatcherOverridesChecks || !friendsPlayer.friends.containsKey(message.dispatcher)
            )
            {
                return PrivateConversationMessageResponse(
                    message.uniqueId, lemonPlayer.getColoredName(ignoreMinequest = true),
                    PrivateConversationMessageResponseContext.TARGET_FRIENDS_ONLY_SETTING
                )
            }
        }

        if (
            messagingSettingValue == MessagingSettingValue.DISABLED &&
            !message.dispatcherOverridesChecks
        )
        {
            return PrivateConversationMessageResponse(
                message.uniqueId, lemonPlayer.getColoredName(ignoreMinequest = true),
                PrivateConversationMessageResponseContext.TARGET_PM_DISABLED
            )
        }

        if (
            profile.ignoring.contains(message.dispatcher) &&
            !message.dispatcherOverridesChecks
        )
        {
            return PrivateConversationMessageResponse(
                message.uniqueId, lemonPlayer.getColoredName(ignoreMinequest = true),
                PrivateConversationMessageResponseContext.TARGET_IGNORING_SENDER
            )
        }

        if (
            bukkitPlayer.hasMetadata("vanished") &&
            !message.dispatcherOverridesChecks
        )
        {
            return PrivateConversationMessageResponse(
                message.uniqueId, lemonPlayer.getColoredName(ignoreMinequest = true),
                PrivateConversationMessageResponseContext.VANISHED
            )
        }

        val messagingSoundsSettingValue = profile
            .setting(
                id = "messages_sounds",
                default = StateSettingValue.ENABLED
            )

        if (messagingSoundsSettingValue == StateSettingValue.ENABLED)
        {
            bukkitPlayer.playSound(
                bukkitPlayer.location,
                XSound.ENTITY_EXPERIENCE_ORB_PICKUP.parseSound(),
                1F, 1F
            )
        }

        profile.lastRecipient = message.dispatcher
        profile.save()

        bukkitPlayer.sendMessage(
            "${CC.GRAY}(From ${message.dispatcherFancyDisplayName}${CC.GRAY}): ${message.content}"
        )

        return PrivateConversationMessageResponse(
            message.uniqueId, lemonPlayer.getColoredName(ignoreMinequest = true),
            PrivateConversationMessageResponseContext.SUCCESSFUL
        )
    }
}
