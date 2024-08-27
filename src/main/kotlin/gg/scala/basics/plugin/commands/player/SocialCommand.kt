package gg.scala.basics.plugin.commands.player

import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.lemon.Lemon
import gg.scala.lemon.LemonConstants
import gg.scala.lemon.metadata.NetworkMetadataDataSync
import net.evilblock.cubed.util.CC
import org.bukkit.command.CommandSender

/**
 * @author GrowlyX
 * @since 10/13/2022
 */
@AutoRegister
@CommandPermission("scbasics.player-commands")
object SocialCommand : ScalaCommand()
{
    @CommandAlias("discord")
    fun onDiscord(sender: CommandSender)
    {
        sender.sendMessage("${CC.SEC}Discord: ${CC.PRI}${NetworkMetadataDataSync.metadata().discord}")
    }

    @CommandAlias("twitter")
    fun onTwitter(sender: CommandSender)
    {
        sender.sendMessage("${CC.SEC}Twitter: ${CC.PRI}${NetworkMetadataDataSync.metadata().twitter}")
    }

    @CommandAlias("website")
    fun onWebsite(sender: CommandSender)
    {
        sender.sendMessage("${CC.SEC}Website: ${CC.PRI}${NetworkMetadataDataSync.metadata().domain}")
    }

    @CommandAlias("store")
    fun onStore(sender: CommandSender)
    {
        sender.sendMessage("${CC.SEC}Store: ${CC.PRI}${NetworkMetadataDataSync.metadata().store}")
    }
}
