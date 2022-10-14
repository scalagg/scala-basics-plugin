package gg.scala.basics.plugin.settings

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
object SettingContainer
{
    private val categories = mutableListOf<SettingCategory>()

    fun categories() = categories

    fun buildEntry(conf: SettingEntryBuilderConf.() -> Unit): SettingEntry
    {
        return entry(conf)
    }
}
