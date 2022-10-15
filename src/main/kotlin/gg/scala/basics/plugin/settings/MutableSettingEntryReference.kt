package gg.scala.basics.plugin.settings

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
data class MutableSettingEntryReference(
    val clazz: String,
    var value: String
)
{
    inline fun <reified T : Enum<T>> map(): T
    {
        if (T::class.java.name != clazz)
        {
            throw IllegalArgumentException(
                "Wrong mapping provided, expected $clazz"
            )
        }

        return enumValueOf(value)
    }
}
