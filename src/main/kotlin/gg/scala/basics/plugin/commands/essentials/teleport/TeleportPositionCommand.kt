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
import org.bukkit.Location
import org.bukkit.command.CommandSender

/**
 * @author GrowlyX
 * @since 10/13/2022
 */
@AutoRegister
@CommandAlias("teleportposition|teleportpos|tppos|tpposition")
@CommandPermission("scbasics.command.teleportposition")
object TeleportPositionCommand : ScalaCommand()
{
    @Default
    fun onDefault(
        sender: ScalaPlayer, location: Location
    )
    {
        val locationFormatted = "${location.x}, ${location.y}, ${location.z}${CC.SEC} in the world ${CC.PRI}${location.world.name}${CC.SEC}"
        sender.teleport(location)

        sender.sendMessage(
            "${CC.SEC}You've teleported to ${CC.PRI}$locationFormatted${CC.SEC}."
        )

        sender.bukkit() performed "teleported to ${location.x}, ${location.y}, ${location.z} in ${location.world.name}"
    }

    @Default
    @CommandCompletion("@players")
    fun onDefault(
        sender: CommandSender, player: LemonPlayer, location: Location
    )
    {
        val locationFormatted = "${location.x}, ${location.y}, ${location.z}${CC.SEC} in the world ${CC.PRI}${location.world.name}${CC.SEC}"
        player.bukkit().teleport(location)

        sender.sendMessage(
            "${CC.SEC}You've teleported ${player.getOriginalColoredName()}${CC.SEC} to ${CC.PRI}$locationFormatted${CC.SEC}."
        )

        sender performed "teleported ${player.name} to ${location.x}, ${location.y}, ${location.z} in ${location.world.name}"
    }
}
