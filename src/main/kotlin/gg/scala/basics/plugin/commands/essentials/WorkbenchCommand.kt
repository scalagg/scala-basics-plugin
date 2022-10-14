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

/**
 * @author GrowlyX
 * @since 10/13/2022
 */
@AutoRegister
@CommandAlias("workbench|wb|craft|crafting")
@CommandPermission("scbasics.command.workbench")
object WorkbenchCommand : ScalaCommand()
{
    @Default
    fun onDefault(player: ScalaPlayer)
    {
        player.bukkit().openWorkbench(player.bukkit().location, true)

        player.sendMessage(
            "${CC.GREEN}You've opened the workbench."
        )

        player performed "opened workbench"
    }

    @Default
    @CommandCompletion("@players")
    @CommandPermission("scbasics.command.clear.other")
    fun onDefaultOther(
        sender: CommandSender, player: LemonPlayer
    )
    {
        player.bukkit().openWorkbench(player.bukkit().location, true)

        sender.sendMessage(
            "${CC.SEC}You've opened the workbench for ${player.getOriginalColoredName()}${CC.SEC}."
        )

        player.bukkit() performed "opened workbench for ${player.name}"
    }
}
