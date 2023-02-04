package gg.scala.basics.plugin.chatfilter

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
data class ChatFilterConfigModel(
    val chatFilters: MutableList<ChatFilter> = mutableListOf()
)

data class ChatFilter(
    val id: String,
    val regex: Regex,
    val testPhrase: String
)
