package gg.scala.basics.plugin.commands.essentials

import gg.scala.basics.plugin.playercolor.PlayerColorMenu
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
@AutoRegister
object ColorCommand : ScalaCommand()
{
    @CommandAlias("color|colors|colours|colour|namecolor|nc")
    @CommandPermission("scbasics.command.color")
    fun onColor(player: Player)
    {
        PlayerColorMenu().openMenu(player)
    }
}
