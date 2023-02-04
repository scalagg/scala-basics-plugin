package gg.scala.basics.plugin.commands.essentials

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
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/13/2022
 */
@AutoRegister
@CommandAlias("fly|iamabird")
@CommandPermission("scbasics.command.fly")
object FlyCommand : ScalaCommand()
{
    private fun Player.toggleFlight()
    {
        apply {
            if (isFlying)
            {
                isFlying = false
            } else
            {
                allowFlight = true
                isFlying = true
            }
        }
    }

    @Default
    fun onDefault(player: ScalaPlayer)
    {
        player.bukkit().toggleFlight()

        player.sendMessage(
            "${if (player.bukkit().isFlying) CC.GREEN else CC.RED}You've ${if (player.bukkit().isFlying) "enabled" else "disabled"} flight."
        )

        player performed "${if (player.bukkit().isFlying) "enabled" else "disabled"} flight"
    }

    @Default
    @CommandCompletion("@players")
    @CommandPermission("scbasics.command.fly.other")
    fun onDefaultOther(
        sender: CommandSender, player: LemonPlayer
    )
    {
        player.bukkit().toggleFlight()

        sender.sendMessage(
            "${CC.SEC}You've ${
                if (player.bukkit().isFlying) "enabled" else "disabled"
            } flight for ${player.getOriginalColoredName()}${CC.SEC}."
        )

        sender performed "${if (player.bukkit().isFlying) "enabled" else "disabled"} flight for ${player.name}"
    }
}
