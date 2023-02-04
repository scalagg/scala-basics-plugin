package gg.scala.basics.plugin.chatfilter

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
data class ChatFilterConfigModel(
    val filteredRegexes: MutableList<ChatFilter> = mutableListOf()
)

data class ChatFilter(
    val regex: Regex,
    val testPhrase: String
)
