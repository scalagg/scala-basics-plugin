package gg.scala.basics.plugin.commands.essentials.teleport

import gg.scala.basics.plugin.commands.bukkit
import gg.scala.basics.plugin.commands.performed
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandCompletion
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Default
import gg.scala.commons.acf.bukkit.contexts.OnlinePlayer
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.lemon.player.LemonPlayer
import net.evilblock.cubed.util.CC
import org.bukkit.command.CommandSender

@AutoRegister
@CommandAlias("teleport|tp")
@CommandPermission("scbasics.command.teleport")
object TeleportCommand : ScalaCommand()
{
    @Default
    fun onDefault(player: ScalaPlayer)
    {
        player.sendMessage(
            "${CC.SEC}Usage: ${CC.PRI}/teleport ${CC.WHITE}<player> [target]"
        )
    }

    @Default
    @CommandCompletion("@players")
    fun onDefault(
        sender: ScalaPlayer, player: LemonPlayer
    )
    {
        sender.bukkit().teleport(player.bukkit())

        sender.sendMessage(
            "${CC.SEC}You've teleported to ${player.getOriginalColoredName()}${CC.SEC}."
        )

        sender.bukkit() performed "teleported to ${player.name}"
    }

    @Default
    @CommandCompletion("@players @players")
    fun onDefault(
        sender: CommandSender, player: LemonPlayer, target: LemonPlayer
    )
    {
        player.bukkit().teleport(target.bukkit())

        sender.sendMessage(
            "${CC.SEC}You've teleported ${player.getOriginalColoredName()}${CC.SEC} to ${target.getOriginalColoredName()}${CC.SEC}."
        )

        sender performed "teleported ${player.name} to ${target.name}"
    }
}
