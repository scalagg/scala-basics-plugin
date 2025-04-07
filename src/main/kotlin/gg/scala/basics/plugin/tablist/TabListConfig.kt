package gg.scala.basics.plugin.tablist

/**
 * @author GrowlyX
 * @since 2/9/2025
 */
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
