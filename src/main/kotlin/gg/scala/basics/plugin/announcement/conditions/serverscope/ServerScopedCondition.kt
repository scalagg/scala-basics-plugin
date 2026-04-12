package gg.scala.basics.plugin.announcement.conditions.serverscope

import gg.scala.basics.plugin.announcement.conditions.AnnouncementDisplayCondition
import gg.scala.basics.plugin.announcement.keys.AnnouncementKey
import gg.scala.commons.agnostic.sync.ServerSync
import gg.scala.flavor.service.Service
import net.kyori.adventure.key.Key
import org.bukkit.entity.Player

@Service
object ServerScopedCondition : AnnouncementDisplayCondition
{
    override fun contextualKey() = true
    override fun contextualKeyNamespace() = "group"
    override fun contextualDisplay(player: Player, key: AnnouncementKey) =
        key.value in ServerSync.getLocalGameServer().groups

    override fun key() = AnnouncementKey("", "")
    override fun shouldDisplay(player: Player) = false
}
