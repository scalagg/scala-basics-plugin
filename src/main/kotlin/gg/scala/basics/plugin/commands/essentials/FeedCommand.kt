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
@CommandAlias("feed|hunger")
@CommandPermission("scbasics.command.feed")
object FeedCommand : ScalaCommand()
{
    @Default
    fun onDefault(player: ScalaPlayer)
    {
        player.bukkit().foodLevel = 20

        player.sendMessage(
            "${CC.GREEN}You've fed yourself."
        )

        player performed "fed themselves"
    }

    @Default
    @CommandCompletion("@players")
    @CommandPermission("scbasics.command.feed.other")
    fun onDefaultOther(
        sender: CommandSender, player: LemonPlayer
    )
    {
        player.bukkit().foodLevel = 20

        sender.sendMessage(
            "${CC.SEC}You've fed ${player.getOriginalColoredName()}${CC.SEC}."
        )

        player.bukkit() performed "fed ${player.name}"
    }
}
