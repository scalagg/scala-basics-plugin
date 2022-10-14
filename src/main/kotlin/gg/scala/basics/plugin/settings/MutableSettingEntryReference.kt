package gg.scala.basics.plugin.settings

import gg.scala.basics.plugin.settings.utils.SettingEnumInternal

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
data class MutableSettingEntryReference(
    val clazz: String,
    var value: String
)
{
    val display by lazy {
        val clazzExists = kotlin
            .runCatching { Class.forName(clazz) }
            .getOrNull()

        clazzExists != null
    }

    inline fun <reified T : Enum<T>> map(): T
    {
        if (!display)
        {
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
