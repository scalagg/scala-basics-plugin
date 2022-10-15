package gg.scala.basics.plugin.commands.player.conversation

import gg.scala.basics.plugin.conversation.PrivateConversationMessageService
import gg.scala.basics.plugin.conversation.message.PrivateConversationMessageResponseContext
import gg.scala.basics.plugin.conversation.settings.MessagingSettingValue
import gg.scala.basics.plugin.profile.BasicsProfileService
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.lemon.filter.ChatMessageFilterHandler
import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandCompletion
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Optional
import gg.scala.lemon.util.QuickAccess
import gg.scala.lemon.util.QuickAccess.username
import net.evilblock.cubed.util.CC
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
@AutoRegister
object MessageCommand : ScalaCommand()
{
    @CommandAlias("reply|r|")
    @CommandPermission("scbasics.conversation-commands")
    fun onReply(
        player: Player, @Optional message: String?
    ): CompletableFuture<Void>
    {
        val profile = BasicsProfileService
            .find(player)
            ?: throw ConditionFailedException(
                "Your profile was incorrectly loaded."
            )

        if (profile.lastRecipient == null)
        {
            throw ConditionFailedException("You have not messaged anyone in the past.")
        }

        if (message == null)
        {
            player.sendMessage(
                "${CC.SEC}You are in a conversation with ${CC.PRI}${profile.lastRecipient!!.username()}${CC.SEC}."
            )
            return CompletableFuture.completedFuture(null)
        }

        return onPrivateMessage(
            player, profile.lastRecipient!!, message
        )
    }

    @CommandCompletion("@players")
    @CommandPermission("scbasics.conversation-commands")
    @CommandAlias("message|msg|tell|t|whisper|w|pm|m")
    fun onPrivateMessage(
        player: Player, target: UUID, message: String
    ): CompletableFuture<Void>
    {
        if (target == player.uniqueId)
        {
            throw ConditionFailedException("You cannot message yourself.")
        }

        return QuickAccess
            .computeColoredName(
                target, target.username()
            )
            .thenApply {
                val profile = BasicsProfileService
                    .find(player)
                    ?: throw ConditionFailedException(
                        "Your profile was incorrectly loaded."
                    )

                val messagingSettingValue = profile
                    .setting(
                        id = "messages",
                        default = MessagingSettingValue.ENABLED
                    )

                if (messagingSettingValue == MessagingSettingValue.DISABLED)
                {
                    throw ConditionFailedException("You have private messages disabled.")
                }

                if (profile.ignoring.contains(target))
                {
                    throw ConditionFailedException("You cannot message a user that you are ignoring.")
                }

                if (
                    ChatMessageFilterHandler.handleMessageFilter(
                        player = player, message = message, target = target
                    )
                )
                {
                    throw ConditionFailedException("You cannot send filtered messages to $it${CC.RED}.")
                }

                return@thenApply it
            }
            .thenCompose { colored ->
                QuickAccess.online(target)
                    .thenApply {
                        Pair(colored, it)
                    }
            }
            .thenAcceptAsync {
                val online = it.second
                val coloredName = it.first

                if (!online)
                {
                    throw ConditionFailedException(
                        String.format(
                            PrivateConversationMessageResponseContext.TARGET_NOT_ONLINE
                                .responseMessage,
                            coloredName
                        )
                    )
                }

                PrivateConversationMessageService.sendMessage(player, target, message)
            }
    }
}
