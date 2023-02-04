package gg.scala.basics.plugin.commands.admin

import gg.scala.basics.plugin.chatfilter.menu.ChatFilterMenu
import gg.scala.commons.acf.CommandHelp
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Default
import gg.scala.commons.acf.annotation.Description
import gg.scala.commons.acf.annotation.HelpCommand
import gg.scala.commons.acf.annotation.Subcommand
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import org.bukkit.command.CommandSender

/**
 * @author GrowlyX
 * @since 2/4/2023
 */
@AutoRegister
@CommandAlias("chatfilter|cf")
@CommandPermission("scbasics.command.chatfilter")
object ChatFilterCommand : ScalaCommand()
{
    @Default
    @HelpCommand
    fun onHelp(help: CommandHelp)
    {
        help.showHelp()
    }

    @Subcommand("entries")
    @Description("View all chat filter entries.")
    fun onEntries(player: ScalaPlayer)
    {
        ChatFilterMenu().openMenu(player.bukkit())
    }

    @Subcommand("add")
    @Description("View all chat filter entries.")
    fun onAdd(player: CommandSender)
    {
        ChatFilterMenu().openMenu(player.bukkit())
    }
}

