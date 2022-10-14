package gg.scala.basics.plugin.commands.player

import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.inject.condition.Named
import gg.scala.lemon.Lemon
import net.evilblock.cubed.util.CC
import org.bukkit.command.CommandSender

/**
 * @author GrowlyX
 * @since 10/13/2022
 */
@AutoRegister
object SocialCommand : ScalaCommand()
{
    @Inject
    @Named("Lemon")
    lateinit var lemon: Lemon

    @CommandAlias("discord")
    @CommandPermission("scbasics.command.socials")
    fun onDiscord(sender: CommandSender)
    {
        sender.sendMessage("${CC.SEC}Discord: ${CC.PRI}${lemon.lemonWebData.discord}")
    }

    @CommandAlias("twitter")
    @CommandPermission("scbasics.command.socials")
    fun onTwitter(sender: CommandSender)
    {
        sender.sendMessage("${CC.SEC}Twitter: ${CC.PRI}${lemon.lemonWebData.twitter}")
    }

    @CommandAlias("website")
    @CommandPermission("scbasics.command.socials")
    fun onWebsite(sender: CommandSender)
    {
        sender.sendMessage("${CC.SEC}Website: ${CC.PRI}${lemon.lemonWebData.domain}")
    }

    @CommandAlias("store")
    @CommandPermission("scbasics.command.socials")
    fun onStore(sender: CommandSender)
    {
        sender.sendMessage("${CC.SEC}Store: ${CC.PRI}${lemon.lemonWebData.store}")
    }
}
