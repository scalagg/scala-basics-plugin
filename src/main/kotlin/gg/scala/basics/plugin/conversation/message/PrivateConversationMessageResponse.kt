package gg.scala.basics.plugin.conversation.message

import gg.scala.aware.conversation.messages.ConversationMessageResponse
import gg.scala.basics.plugin.conversation.message.PrivateConversationMessageResponseContext
import java.util.*

data class PrivateConversationMessageResponse(
    private val conversationIdInternal: UUID,
    val targetedReceiverFancyName: String?,
    val responseStatus: PrivateConversationMessageResponseContext
) : ConversationMessageResponse(conversationIdInternal)
