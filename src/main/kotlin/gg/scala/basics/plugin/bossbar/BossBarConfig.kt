package gg.scala.basics.plugin.bossbar

/**
 * @author GrowlyX
 * @since 2/9/2025
 */
data class BossBarConfig(
    val internalConfigs: MutableMap<String, ServerBossBar> = mutableMapOf(
        "default" to ServerBossBar("default")
    )
)
{
    fun configs() = internalConfigs.values.toList()
    fun update(config: ServerBossBar)
    {
        internalConfigs[config.id] = config
    }
}
