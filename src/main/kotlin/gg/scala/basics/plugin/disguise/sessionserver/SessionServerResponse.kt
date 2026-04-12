package gg.scala.basics.plugin.disguise.sessionserver

data class SessionServerResponse(
    val id: String,
    val name: String,
    val properties: List<SessionServerProperty>
)
