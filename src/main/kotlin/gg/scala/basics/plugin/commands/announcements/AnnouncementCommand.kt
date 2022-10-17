package gg.scala.basics.plugin.commands.announcements

import gg.scala.basics.plugin.announcement.menu.AnnouncementMenu
import gg.scala.commons.acf.CommandHelp
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Default
import gg.scala.commons.acf.annotation.Description
import gg.scala.commons.acf.annotation.HelpCommand
import gg.scala.commons.acf.annotation.Subcommand
import gg.scala.commons.annotations.commands.AssignPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
@AutoRegister
@CommandAlias("announcement|announcements|ann")
@CommandPermission("scbasics.command.announcements")
object AnnouncementCommand : ScalaCommand()
{
    @Default
    @HelpCommand
    fun onHelp(help: CommandHelp)
    {
        help.showHelp()
    }

    @AssignPermission
    @Subcommand("adminpanel")
    @Description("Enter the announcements admin panel.")
    fun onAdminPanel(player: ScalaPlayer)
    {
        AnnouncementMenu().openMenu(player.bukkit())
    }
}
