package gg.scala.basics.plugin.commands

import gg.scala.commons.acf.CommandHelp
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Default
import gg.scala.commons.acf.annotation.HelpCommand
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
@AutoRegister
@CommandAlias("announcement|announcements")
@CommandPermission("scbasics.command.announcements")
object AnnouncementCommand : ScalaCommand()
{
    @Default
    @HelpCommand
    fun onHelp(help: CommandHelp)
    {
        help.showHelp()
    }
}
