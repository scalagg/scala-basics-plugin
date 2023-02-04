package gg.scala.basics.plugin.chatfilter

import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.lemon.filter.ChatMessageFilterHandler
import gg.scala.lemon.filter.phrase.MessagePhraseFilter
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 2/4/2023
 */
@Service
object ChatFilterPhraseDetectService : MessagePhraseFilter
{
    @Configure
    fun configure()
    {
        ChatMessageFilterHandler.phraseFilters.add(this)
    }

    override fun isFiltered(player: Player, word: String): Boolean
    {
        return ChatFilterService.configModel()
            .chatFilters
            .any {
                it.regex.matches(word)
            }
    }

    override fun loadResources()
    {
    }
}
