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

@AutoRegister
@CommandAlias("clear|c|clearinv")
@CommandPermission("scbasics.command.clear")
object ClearCommand : ScalaCommand()
{
    @Default
    fun onDefault(player: ScalaPlayer)
    {
        player.bukkit().inventory.clear()
        player.bukkit().updateInventory()

        player.sendMessage(
            "${CC.GREEN}You've cleared your inventory."
        )

        player performed "cleared inventory"
    }

    @Default
    @CommandCompletion("@players")
    @CommandPermission("scbasics.command.clear.other")
    fun onDefaultOther(
        sender: CommandSender, player: LemonPlayer
    )
    {
        player.bukkit().inventory.clear()
        player.bukkit().updateInventory()

        sender.sendMessage(
            "${CC.SEC}You've cleared ${player.getOriginalColoredName()}'s${CC.SEC} inventory."
        )

        sender performed "cleared ${player.name}'s inventory"
    }
}
