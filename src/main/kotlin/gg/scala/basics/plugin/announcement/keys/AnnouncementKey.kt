package gg.scala.basics.plugin.announcement.keys

import gg.scala.basics.plugin.announcement.conditions.AnnouncementConditions
import gg.scala.basics.plugin.announcement.conditions.AnnouncementDisplayCondition
import net.evilblock.cubed.util.CC
import org.bukkit.entity.Player

data class AnnouncementKey(
    val namespace: String,
    val value: String,
    var logicCondition: LogicCondition = LogicCondition.And,
    var contextual: Boolean = false,
    var referenceKeyString: String? = null
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

        val split = referenceKeyString!!.split(":")
        val contextualCondition = AnnouncementConditions.conditions[namespace]
            ?: return false

        return contextualCondition.contextualDisplay(player, AnnouncementKey(split[0], split[1]))
    }

    fun getMenuPrefix() : String
    {
        return if (logicCondition == LogicCondition.Not || logicCondition == LogicCondition.And)
            "${logicCondition.color}(${logicCondition.displayName}) "
        else "${CC.WHITE}${referenceKeyString ?: "Unknown"} ${logicCondition.color}(${logicCondition.displayName}) "
    }

    fun isCondition(other: LogicCondition) = logicCondition == other
}
