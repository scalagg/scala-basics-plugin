package gg.scala.basics.plugin.conversation

import gg.scala.aware.conversation.ConversationContinuation
import gg.scala.basics.plugin.conversation.message.PrivateConversationMessage
import gg.scala.basics.plugin.conversation.message.PrivateConversationMessageResponse
import gg.scala.basics.plugin.conversation.message.PrivateConversationMessageResponseContext
import gg.scala.basics.plugin.profile.BasicsProfileService
import gg.scala.lemon.handler.PlayerHandler
import gg.scala.lemon.player.metadata.Metadata
import gg.scala.lemon.util.CubedCacheUtil
import net.evilblock.cubed.util.CC
import org.bukkit.Bukkit

/**
 * @author GrowlyX
 * @since 1/17/2022
 */
object PrivateConversationMessageReceiver : (PrivateConversationMessage, PrivateConversationMessageResponse) -> ConversationContinuation
{
    override fun invoke(
        message: PrivateConversationMessage,
        reply: PrivateConversationMessageResponse
    ): ConversationContinuation
    {
        val bukkitPlayer = Bukkit
            .getPlayer(message.dispatcher)
            ?: return ConversationContinuation.END

        val response = reply.responseStatus

        val fancyName = reply.targetedReceiverFancyName
            ?: CubedCacheUtil.fetchName(message.targetedReceiver)!!

        if (response != PrivateConversationMessageResponseContext.SUCCESSFUL)
        {
            if (response == PrivateConversationMessageResponseContext.TARGET_NOT_ONLINE)
            {
                return ConversationContinuation.CONTINUE
            }

            bukkitPlayer.sendMessage("${CC.RED}${
                String.format(response.responseMessage, fancyName)
            }")

            return ConversationContinuation.END
        }

        bukkitPlayer.sendMessage(
            "${CC.GRAY}(To ${fancyName}${CC.GRAY}): ${message.content}"
        )

        val profile = BasicsProfileService
            .find(bukkitPlayer)
            ?: return ConversationContinuation.END

        profile.lastRecipient = message.targetedReceiver
        profile.lastRecipientMessageTimestamp = System.currentTimeMillis()
        profile.save()

        return ConversationContinuation.END
    }
}
