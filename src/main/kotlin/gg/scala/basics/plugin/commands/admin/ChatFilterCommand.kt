package gg.scala.basics.plugin.commands.admin

import gg.scala.basics.plugin.chatfilter.ChatFilter
import gg.scala.basics.plugin.chatfilter.ChatFilterService
import gg.scala.basics.plugin.chatfilter.menu.ChatFilterMenu
import gg.scala.commons.acf.CommandHelp
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandCompletion
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Default
import gg.scala.commons.acf.annotation.Description
import gg.scala.commons.acf.annotation.HelpCommand
import gg.scala.commons.acf.annotation.Subcommand
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import net.evilblock.cubed.menu.menus.ConfirmMenu
import net.evilblock.cubed.util.CC
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

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

    @Subcommand("remove")
    @CommandCompletion("@chatfilters")
    @Description("Remove a chat filter.")
    fun onRemove(player: CommandSender, filter: ChatFilter)
    {
        val performRemoval = {
            val model = ChatFilterService.configModel()
            model.removeWithId(filter.id)

            ChatFilterService
                .applyKvUpdate(model)

            player.sendMessage(
                "${CC.GREEN}You've removed the chat filter entry: ${CC.GOLD}${filter.id}"
            )
        }

        if (player is Player)
        {
            ConfirmMenu(
                "Confirm entry removal?", confirm = true
            ) {
                if (it) performRemoval()
            }
        } else
        {
            performRemoval()
        }
    }
}

