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
object TeleportHereCommand : ScalaCommand()
{
    @CommandCompletion("@players")
    @CommandAlias("teleporthere|tphere|tph")
    @CommandPermission("scbasics.command.teleporthere")
    fun onTpHere(
        sender: ScalaPlayer, player: LemonPlayer
    )
    {
        player.bukkit().teleport(sender.bukkit())

        sender.sendMessage(
            "${CC.SEC}You've teleported ${player.getOriginalColoredName()}${CC.SEC} to you."
        )

        sender.bukkit() performed "teleported ${player.name} to themselves"
    }
}
