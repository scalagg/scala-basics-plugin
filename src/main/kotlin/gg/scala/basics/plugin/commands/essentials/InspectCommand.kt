package gg.scala.basics.plugin.commands.essentials

import gg.scala.basics.plugin.commands.bukkit
import gg.scala.basics.plugin.commands.performed
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandCompletion
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.lemon.menu.modmode.InspectionMenu
import gg.scala.lemon.player.LemonPlayer
import net.evilblock.cubed.util.CC
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
@AutoRegister
object InspectCommand : ScalaCommand()
{
    @CommandCompletion("@players")
    @CommandAlias("invsee|inspect|viewinventory|viewinv|inv")
    @CommandPermission("scbasics.command.invsee")
    fun onInspect(player: Player, target: LemonPlayer)
    {
        player.sendMessage("${CC.SEC}Viewing ${target.getOriginalColoredName()}'s${CC.SEC} inventory...")
        InspectionMenu(target.bukkit()).openMenu(player)

        player performed "inspected ${target.name}"
    }
}
