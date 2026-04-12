package gg.scala.basics.plugin.bossbar

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
