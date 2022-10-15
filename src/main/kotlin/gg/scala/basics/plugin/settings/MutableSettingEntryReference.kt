package gg.scala.basics.plugin.settings

import gg.scala.basics.plugin.settings.utils.SettingEnumInternal
import net.kyori.adventure.util.TriState

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
data class MutableSettingEntryReference(
    val clazz: String,
    var value: String
)
{
    var display = TriState.NOT_SET

    inline fun <reified T : Enum<T>> map(): T
    {
        if (display == TriState.FALSE || display == TriState.NOT_SET)
        {
            if (display == TriState.NOT_SET)
            {
                display = TriState.byBoolean(
                    kotlin
                        .runCatching { Class.forName(clazz) }
                        .getOrNull() != null
                )
                return map<T>()
            }

            return SettingEnumInternal.getFirstConstant(T::class.java)!!
        }

        if (T::class.java.name != clazz)
        {
            throw IllegalArgumentException(
                "Wrong mapping provided, expected $clazz"
            )
        }

        return enumValueOf(value)
    }
}
