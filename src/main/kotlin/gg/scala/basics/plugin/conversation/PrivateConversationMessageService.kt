package gg.scala.basics.plugin.conversation

import gg.scala.aware.conversation.ConversationFactory
import gg.scala.aware.conversation.ConversationFactoryBuilder
import gg.scala.basics.plugin.conversation.message.PrivateConversationMessage
import gg.scala.basics.plugin.conversation.message.PrivateConversationMessageResponse
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.lemon.handler.PlayerHandler
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author GrowlyX
 * @since 1/17/2022
 */
@Service
object PrivateConversationMessageService
{
    private lateinit var agent: ConversationFactory<PrivateConversationMessage, PrivateConversationMessageResponse>

    @Configure
    fun configure()
    {
        agent = ConversationFactoryBuilder
            .of<PrivateConversationMessage, PrivateConversationMessageResponse>()
            .channel("messaging-agent")
            .timeout(2L, TimeUnit.SECONDS) {}
            .response(PrivateConversationMessageResponder)
            .receive(PrivateConversationMessageReceiver)
            .build()

        agent.configure().toCompletableFuture().join()
    }

    fun sendMessage(player: Player, target: UUID, content: String)
    {
        val coloredName = PlayerHandler.find(player.uniqueId)!!
            .getColoredName(ignoreMinequest = true)

        val message = PrivateConversationMessage(
            player.uniqueId,
            player.hasPermission("scstaff.staff-member"),
            coloredName, target, content
        )


        agent.distribute(message)
    }
}
