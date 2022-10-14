package gg.scala.basics.plugin.commands.admin

import gg.scala.basics.plugin.commands.performed
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Syntax
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.lemon.util.QuickAccess.sendGlobalBroadcast
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.Color.translate
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import java.util.concurrent.CompletableFuture

/**
 * @author GrowlyX
 * @since 11/12/2021
 */
@AutoRegister
object BroadcastCommand : ScalaCommand()
{
    @CommandAlias("broadcast|bc")
    @CommandPermission("scbasics.command.broadcast")
    fun onAlert(sender: CommandSender, message: String)
    {
        Bukkit.broadcastMessage(
            "${CC.GRAY}[${CC.PRI}Alert${CC.GRAY}] ${CC.RESET}${translate(message)}"
        )

        sender performed "broadcasted local message"
    }
}
