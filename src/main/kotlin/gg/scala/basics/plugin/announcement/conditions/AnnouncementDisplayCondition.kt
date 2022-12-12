package gg.scala.basics.plugin.announcement.conditions

import net.kyori.adventure.key.Key
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 12/12/2022
 */
interface AnnouncementDisplayCondition
{
    fun key(): Key
    fun shouldDisplay(player: Player): Boolean
}
