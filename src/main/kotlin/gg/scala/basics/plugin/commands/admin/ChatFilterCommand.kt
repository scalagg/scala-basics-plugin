package gg.scala.basics.plugin.commands.admin

import gg.scala.basics.plugin.chatfilter.ChatFilter
import gg.scala.basics.plugin.chatfilter.ChatFilterService
import gg.scala.basics.plugin.chatfilter.menu.ChatFilterMenu
import gg.scala.commons.acf.CommandHelp
import gg.scala.commons.acf.ConditionFailedException
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
            val model = ChatFilterService.cached()
            model.removeWithId(filter.id)

            ChatFilterService
                .sync(model)

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

    @Subcommand("add")
    @Description("Add a chat filter.")
    fun onAdd(
        player: CommandSender, id: String,
        pattern: String, testPhrase: String
    )
    {
        if (ChatFilterService.cached().findById(id) != null)
        {
            throw ConditionFailedException("The id $id is already in use!")
        }

        val regex = pattern.toRegex()

        if (!regex.matches(testPhrase))
        {
            throw ConditionFailedException(
                "Your test phrase does not match the provided pattern!"
            )
        }

        val model = ChatFilterService.cached()
        model.chatFilters.add(
            ChatFilter(id, regex, testPhrase)
        )

        ChatFilterService.sync(model)

        player.sendMessage(
            "${CC.GREEN}You've added the chat filter entry ${CC.GRAY}(globally)${CC.GREEN}: ${CC.GOLD}$id"
        )
    }
}

