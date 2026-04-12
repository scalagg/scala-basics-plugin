package gg.scala.basics.plugin.settings

object SettingContainer
{
    private val categories = mutableListOf<SettingCategory>()

    fun categories() = categories

    fun buildEntry(
        conf: SettingEntryBuilderConf.() -> Unit
    ): SettingEntry
    {
        return entry(conf)
    }
}
