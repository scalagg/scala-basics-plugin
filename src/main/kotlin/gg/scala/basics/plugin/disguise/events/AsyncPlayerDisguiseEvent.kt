package gg.scala.basics.plugin.disguise.events

import gg.scala.basics.plugin.disguise.sessionserver.SessionServerResponse
import gg.scala.commons.event.StatefulEvent
import org.bukkit.entity.Player

data class AsyncPlayerDisguiseEvent(
    val player: Player,
    val profile: SessionServerResponse
) : StatefulEvent()
