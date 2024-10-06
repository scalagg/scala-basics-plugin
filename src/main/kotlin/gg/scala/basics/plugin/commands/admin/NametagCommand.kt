package gg.scala.basics.plugin.commands.admin

import gg.scala.basics.plugin.nametag.RainbowNametagService
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.lemon.player.LemonPlayer
import gg.scala.commons.acf.CommandHelp
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandCompletion
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Default
import gg.scala.commons.acf.annotation.Description
import gg.scala.commons.acf.annotation.HelpCommand
import gg.scala.commons.acf.annotation.Optional
import gg.scala.commons.acf.annotation.Subcommand
import gg.scala.commons.acf.annotation.Syntax
import gg.scala.commons.annotations.commands.AssignPermission
import net.evilblock.cubed.nametag.NametagHandler
import net.evilblock.cubed.util.CC
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 11/25/2021
 */
@CommandAlias("nametag")
@CommandPermission("scbasics.command.nametag")
object NametagCommand : ScalaCommand()
{
    @Default
    @HelpCommand
    fun onHelp(help: CommandHelp)
    {
        help.showHelp()
    }

    @AssignPermission
    @Syntax("[player]")
    @Subcommand("rainbow")
    @CommandCompletion("@players")
    @Description("Override you current nametag to a fancy rainbow one.")
    fun onRainbow(player: Player, @Optional target: LemonPlayer?)
    {
        val uniqueId = target?.uniqueId ?: player.uniqueId

        if (RainbowNametagService.rainbowNametagEnabled.contains(uniqueId))
        {
            RainbowNametagService.rainbowNametagEnabled.remove(uniqueId)

            player.sendMessage("${CC.SEC}Disabled rainbow nametag for: ${
                target?.getColoredName() ?: player.displayName
            }")

            NametagHandler.reloadPlayer(target?.bukkitPlayer ?: player)
        } else
        {
            RainbowNametagService.rainbowNametagEnabled.add(uniqueId)

            player.sendMessage("${CC.SEC}Enabled rainbow nametag for: ${
                target?.getColoredName() ?: player.displayName
            }")
        }
    }
}
