package gg.scala.basics.plugin.announcement.conditions.serverscope

import gg.scala.basics.plugin.announcement.conditions.AnnouncementDisplayCondition
import gg.scala.basics.plugin.announcement.keys.AnnouncementKey
import gg.scala.commons.agnostic.sync.ServerSync
import gg.scala.flavor.service.Service
import gg.scala.lemon.handler.PlayerHandler
import gg.scala.lemon.util.QuickAccess
import net.kyori.adventure.key.Key
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 4/16/2023
 */
@Service
object TimestampCondition : AnnouncementDisplayCondition
{
    override fun contextualKey() = true
    override fun contextualKeyNamespace() = "timestamp"
    override fun contextualDisplay(player: Player, key: AnnouncementKey) =
        System.currentTimeMillis() <= key.value.toLong()

    override fun key() = AnnouncementKey("", "")
    override fun shouldDisplay(player: Player) = false
}
