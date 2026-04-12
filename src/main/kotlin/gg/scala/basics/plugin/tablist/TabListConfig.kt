package gg.scala.basics.plugin.tablist

data class TabListConfig(
    val internalConfigs: MutableMap<String, ServerTabList> = mutableMapOf(
        "default" to ServerTabList("default")
    )
)
{
    fun configs() = internalConfigs.values.toList()
    fun update(config: ServerTabList)
    {
        internalConfigs[config.id] = config
    }
}
