package gg.scala.basics.plugin.chatfilter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import gg.scala.aware.message.AwareMessage
import gg.scala.aware.thread.AwareThreadContext
import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.basics.plugin.cache.SharedAwareService
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import net.evilblock.cubed.serializers.Serializers

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
                object : TypeAdapter<Regex>()
                {
                    override fun write(writer: JsonWriter, regex: Regex)
                    {
                        writer.beginObject()
                        writer.name("pattern").value(regex.pattern)
                        writer.endObject()
                    }

                    override fun read(reader: JsonReader): Regex
                    {
                        reader.beginObject()
                        return reader.nextString().toRegex()
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
