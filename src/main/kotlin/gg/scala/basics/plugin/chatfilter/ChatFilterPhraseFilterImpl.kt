package gg.scala.basics.plugin.chatfilter

import gg.scala.lemon.filter.phrase.MessagePhraseFilter
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 2/4/2023
 */
object ChatFilterPhraseFilterImpl : MessagePhraseFilter
{
    override fun isFiltered(player: Player, word: String): Boolean
    {
        return ChatFilterService.configModel()
            .filteredRegexes
            .any {
                it.regex.matches(word)
            }
    }

    override fun loadResources()
    {
    }
}
