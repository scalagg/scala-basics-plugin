package gg.scala.basics.plugin.commands.player

import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.Syntax
import gg.scala.commons.agnostic.sync.server.ServerContainer
import gg.scala.commons.agnostic.sync.server.impl.GameServer
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.lemon.redirection.impl.VelocityRedirectSystem
import net.evilblock.cubed.util.CC
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 12/14/2021 14:28
 */
@AutoRegister
object JoinCommand : ScalaCommand()
{
    @Syntax("<server>")
    @CommandAlias("join")
    fun onJoin(player: Player, serverName: String)
    {
        val server = ServerContainer.allServers<GameServer>()
            .find {
                it.id.equals(serverName, true)
            }
            ?: throw ConditionFailedException(
                "There is no online server named ${CC.YELLOW}$serverName${CC.RED}."
            )

        if (
            server.getWhitelisted() == true &&
            server.getWhitelistedPlayers()?.contains(player.uniqueId) != true
        )
        {
            throw ConditionFailedException("You cannot join a server which is whitelisted.")
        }

        val isJoinable = server
            .getMetadataValue(
                "queue", "joinable"
            ) ?: false

        val isGameServer = server
            .getMetadataValue(
                "game", "game-server"
            ) ?: false

        if (!isGameServer)
        {
            if (!isJoinable && !player.hasPermission("scbasics.command.join.bypass"))
            {
                throw ConditionFailedException("You don't have permission to join ${CC.YELLOW}$serverName${CC.RED}.")
            }
        }

        VelocityRedirectSystem.redirect(player, server.id)
    }
}
