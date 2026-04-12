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
@CommandAlias("heal")
@CommandPermission("scbasics.command.heal")
object HealCommand : ScalaCommand()
{
    @Default
    fun onDefault(player: ScalaPlayer)
    {
        player.bukkit().health = player.bukkit().maxHealth

        player.sendMessage(
            "${CC.GREEN}You've healed yourself."
        )

        player performed "healed themselves"
    }

    @Default
    @CommandCompletion("@players")
    @CommandPermission("scbasics.command.heal.other")
    fun onDefaultOther(
        sender: CommandSender, player: LemonPlayer
    )
    {
        player.bukkit().health = player.bukkit().maxHealth

        sender.sendMessage(
            "${CC.SEC}You've healed ${player.getOriginalColoredName()}${CC.SEC}."
        )

        sender performed "healed ${player.name}"
    }
}
