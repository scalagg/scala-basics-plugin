package gg.scala.basics.plugin.chatfilter

import com.google.gson.*
import net.evilblock.cubed.serializers.Serializers
import java.lang.reflect.Type

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

fun configureChatFilterSerializers()
{
    Serializers.create {
        registerTypeAdapter(
            Regex::class.java,
            object : JsonSerializer<Regex>, JsonDeserializer<Regex>
            {
                override fun serialize(
                    p0: Regex, p1: Type?, p2: JsonSerializationContext?
                ): JsonElement
                {
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("pattern", p0.pattern)
                    return jsonObject
                }

                override fun deserialize(
                    p0: JsonElement,
                    p1: Type?,
                    p2: JsonDeserializationContext?
                ): Regex
                {
                    return p0.asJsonObject
                        .get("pattern")
                        .asString.toRegex()
                }
            }
        )
    }
}
