package gg.scala.basics.plugin.announcement.conditions.serverscope

import gg.scala.basics.plugin.announcement.conditions.AnnouncementDisplayCondition
import gg.scala.commons.agnostic.sync.ServerSync
import gg.scala.flavor.service.Service
import net.kyori.adventure.key.Key
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 4/16/2023
 */
@Service
object ServerScopedCondition : AnnouncementDisplayCondition
{
    override fun contextualKey() = true
    override fun contextualKeyNamespace() = "group"
    override fun contextualDisplay(player: Player, key: Key) =
        key.value() in ServerSync.getLocalGameServer().groups

    override fun key() = Key.key("i", "i")
    override fun shouldDisplay(player: Player) = false
}
