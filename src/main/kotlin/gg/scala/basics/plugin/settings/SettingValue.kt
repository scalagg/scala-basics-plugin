package gg.scala.basics.plugin.settings

import com.google.gson.JsonObject
import gg.scala.basics.plugin.settings.interfaces.DisplayEnsure
import me.lucko.helper.gson.GsonSerializable

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
interface SettingValue : DisplayEnsure, GsonSerializable
{
    val displayName: String

    override fun serialize() =
        JsonObject()
            .apply {
                val enum = (this@SettingValue as Enum<*>)

                addProperty(
                    "name", enum.name
                )
            }
}
