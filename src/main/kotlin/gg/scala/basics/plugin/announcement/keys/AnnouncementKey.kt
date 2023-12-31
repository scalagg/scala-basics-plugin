package gg.scala.basics.plugin.announcement.keys

import gg.scala.basics.plugin.announcement.conditions.AnnouncementConditions
import gg.scala.basics.plugin.announcement.conditions.AnnouncementDisplayCondition
import org.bukkit.entity.Player

data class AnnouncementKey(
    val namespace: String,
    val value: String,
    var logicCondition: LogicCondition = LogicCondition.And,
    val contextual: Boolean = false,
    val referenceKeyString: String? = null
)
{
    fun checkReference(player: Player): Boolean
    {
        if (referenceKeyString == null)
        {
            return false
        }

        if (!contextual)
        {
            return false
        }

        val split = referenceKeyString.split(":")
        val contextualCondition = AnnouncementConditions.conditions[namespace]
            ?: return false

        return contextualCondition.contextualDisplay(player, AnnouncementKey(split[0], split[1]))
    }

    fun isCondition(other: LogicCondition) = logicCondition == other
}
