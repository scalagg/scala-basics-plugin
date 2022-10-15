package gg.scala.basics.plugin.conversation.message

import gg.scala.aware.conversation.messages.ConversationMessageResponse
import gg.scala.basics.plugin.conversation.message.PrivateConversationMessageResponseContext
import java.util.*

/**
 * @author GrowlyX
 * @since 1/17/2022
 */
data class PrivateConversationMessageResponse(
    private val conversationIdInternal: UUID,
    val targetedReceiverFancyName: String?,
    val responseStatus: PrivateConversationMessageResponseContext
) : ConversationMessageResponse(conversationIdInternal)
