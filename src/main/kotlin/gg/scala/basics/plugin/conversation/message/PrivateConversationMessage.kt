package gg.scala.basics.plugin.conversation.message

import gg.scala.aware.conversation.messages.ConversationMessage
import java.util.*

data class PrivateConversationMessage(
    val dispatcher: UUID,
    val dispatcherOverridesChecks: Boolean,
    val dispatcherFancyDisplayName: String,
    val targetedReceiver: UUID,
    val content: String
) : ConversationMessage()
