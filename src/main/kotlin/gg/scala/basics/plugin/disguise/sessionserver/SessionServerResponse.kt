package gg.scala.basics.plugin.disguise.sessionserver

/**
 * @author GrowlyX
 * @since 5/14/2023
 */
data class SessionServerResponse(
    val id: String,
    val name: String,
    val properties: List<SessionServerProperty>
)
