package gg.scala.basics.plugin.chatfilter

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
data class ChatFilterConfigModel(
    val chatFilters: MutableList<ChatFilter> = mutableListOf()
)
{
    fun findById(id: String) = chatFilters
        .firstOrNull {
            it.id.equals(id, true)
        }

    fun removeWithId(id: String) = chatFilters
        .removeIf {
            it.id.equals(id, true)
        }
}

data class ChatFilter(
    val id: String,
    val regex: Regex,
    val testPhrase: String
)
