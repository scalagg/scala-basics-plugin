package gg.scala.basics.plugin.conversation.message

import gg.scala.aware.conversation.messages.ConversationMessage
import java.util.*

/**
 * @author GrowlyX
 * @since 1/17/2022
 */
data class PrivateConversationMessage(
    val dispatcher: UUID,
    val dispatcherOverridesChecks: Boolean,
    val dispatcherFancyDisplayName: String,
    val targetedReceiver: UUID,
    val content: String
) : ConversationMessage()
