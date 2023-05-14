package gg.scala.basics.plugin.disguise.sessionserver

import java.util.UUID

/**
 * @author GrowlyX
 * @since 5/14/2023
 */
data class SessionServerResponse(
    val id: UUID,
    val name: String,
    val properties: List<SessionServerProperty>
)

