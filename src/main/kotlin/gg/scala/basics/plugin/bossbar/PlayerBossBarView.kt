package gg.scala.basics.plugin.bossbar

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

data class PlayerBossBarView(
    val audience: Audience,
    var iteration: Int = 0,
    var activeBossBar: BossBar? = null,
    var lastUpdate: Long = 0L,
)
{
    fun dispose() = activeBossBar?.apply {
        audience.hideBossBar(this)
    }

    fun update(content: String, progress: Float, bossBar: ServerBossBar)
    {
        lastUpdate = System.currentTimeMillis()

        if (activeBossBar != null)
        {
            activeBossBar!!.apply {
                name(LegacyComponentSerializer.legacySection().deserialize(content))
                progress(progress.coerceIn(0.0f, 1.0f))
                color(bossBar.color)
            }
            return
        }

        val newBossBar = BossBar.bossBar(
            LegacyComponentSerializer.legacySection().deserialize(content),
            progress.coerceIn(0.0f, 1.0f),
            bossBar.color,
            BossBar.Overlay.PROGRESS
        )

        activeBossBar = newBossBar
        audience.showBossBar(newBossBar)
    }
}
