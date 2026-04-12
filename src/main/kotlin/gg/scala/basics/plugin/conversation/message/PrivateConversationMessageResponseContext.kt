package gg.scala.basics.plugin.conversation.message

import net.evilblock.cubed.util.CC

enum class PrivateConversationMessageResponseContext(
    val responseMessage: String
)
{
    TARGET_FRIENDS_ONLY_SETTING("${CC.YELLOW}%s ${CC.RED}has private messages set to friends-only."),
    TARGET_PM_DISABLED("${CC.YELLOW}%s ${CC.RED}has private messages disabled."),
    TARGET_IGNORING_SENDER("${CC.YELLOW}%s ${CC.RED}is ignoring your messages."),
    TARGET_NOT_ONLINE("${CC.RED}No player matching ${CC.YELLOW}%s${CC.RED} could be found on the network."),
    VANISHED("${CC.RED}No player matching ${CC.YELLOW}%s${CC.RED} could be found on the network."),
    SUCCESSFUL("");

    override fun toString() = responseMessage
}
