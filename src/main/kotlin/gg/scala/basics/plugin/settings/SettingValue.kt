package gg.scala.basics.plugin.settings

import com.google.gson.JsonObject
import gg.scala.basics.plugin.settings.interfaces.DisplayPredicate
import me.lucko.helper.gson.GsonSerializable

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
interface SettingValue<SELF : SettingValue<SELF>> : DisplayPredicate, GsonSerializable
{
    val displayName: String
    val default: SELF

    override fun serialize() =
        JsonObject()
            .apply {
                val enum = (this@SettingValue as Enum<*>)

                addProperty(
                    "name", enum.name
                )
            }
}
