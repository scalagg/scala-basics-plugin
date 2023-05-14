package gg.scala.basics.plugin.disguise.events

import gg.scala.basics.plugin.disguise.sessionserver.SessionServerResponse
import gg.scala.commons.event.StatefulEvent
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 5/14/2023
 */
data class AsyncPlayerDisguiseEvent(
    val player: Player,
    val profile: SessionServerResponse
) : StatefulEvent()
