package gg.scala.basics.plugin.tablist.animation

import gg.scala.commons.scoreboard.TextAnimator
import gg.scala.commons.scoreboard.animations.TextFadeAnimation
import gg.scala.flavor.service.Close
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.lemon.metadata.NetworkMetadataDataSync
import net.evilblock.cubed.util.CC
import org.bukkit.ChatColor
import java.util.*

/**
 * @date 4/8/2025
 */
@Service
object TabAnimationService
{
    private val titleAnimator = TextAnimator.of(
        TextFadeAnimation(
            "${CC.BOLD}${NetworkMetadataDataSync.cached().serverName.uppercase()}",
            ChatColor.GOLD,
            ChatColor.YELLOW,
            ChatColor.WHITE
        )
    )

    fun getCurrentTitle() = titleAnimator.current().text

    @Configure
    fun configure()
    {
        titleAnimator.schedule()
    }

    @Close
    fun close()
    {
        titleAnimator.dispose()
    }
}
