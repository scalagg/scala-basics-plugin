package gg.scala.basics.plugin.chatfilter

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import gg.scala.aware.message.AwareMessage
import gg.scala.aware.thread.AwareThreadContext
import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.basics.plugin.cache.SharedAwareService
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import net.evilblock.cubed.serializers.Serializers
import java.lang.IllegalStateException
import java.lang.reflect.Type

/**
 * @author GrowlyX
 * @since 10/16/2022
 */
@Service
object ChatFilterService
{
    @Inject
    lateinit var plugin: ScalaBasicsPlugin

    private var cached: ChatFilterConfigModel =
        ChatFilterConfigModel()

    fun configModel() = cached

    @Configure
    fun configure()
    {
        plugin.aware
            .listen(
                ChatFilterKeys.updateKey
            ) {
                reload()
            }

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

        reload()
    }

    private fun reload()
    {
        val model = SharedAwareService
            .cache().sync()
            .get(
                ChatFilterKeys.storeKey
            )

        if (model == null)
        {
            cached = ChatFilterConfigModel()
            SharedAwareService
                .cache().sync()
                .set(
                    ChatFilterKeys.storeKey,
                    Serializers.gson.toJson(cached)
                )

            pushKvUpdate()
            return
        }

        cached = Serializers.gson
            .fromJson(
                model, ChatFilterConfigModel::class.java
            )
            ?: ChatFilterConfigModel()
    }

    fun applyKvUpdate(
        newModel: ChatFilterConfigModel
    )
    {
        cached = newModel
        SharedAwareService
            .cache().sync()
            .set(
                ChatFilterKeys.storeKey,
                Serializers.gson.toJson(cached)
            )

        pushKvUpdate()
    }

    private fun pushKvUpdate()
    {
        AwareMessage
            .of(
                ChatFilterKeys.updateKey,
                plugin.aware
            )
            .publish(
                AwareThreadContext.SYNC
            )
    }
}
