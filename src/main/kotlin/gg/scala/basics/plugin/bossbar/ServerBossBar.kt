package gg.scala.basics.plugin.bossbar

import gg.scala.commons.scoping.ServerScoped
import net.evilblock.cubed.util.CC
import net.kyori.adventure.bossbar.BossBar

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
data class ServerBossBar(
    val id: String,
    var contents: List<String> = listOf(
        "${CC.B_GOLD}CURRENTLY IN BETA"
    ),
    var progress: Float = 1.0f,
    var isDynamicProgress: Boolean = true,
    var interval: Long? = null,
    var color: BossBar.Color = BossBar.Color.RED,
    var static: Boolean = false,
    override var scopes: Set<String> = setOf("global")
) : ServerScoped
