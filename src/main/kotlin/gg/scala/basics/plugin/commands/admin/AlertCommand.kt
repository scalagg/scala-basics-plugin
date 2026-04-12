package gg.scala.basics.plugin.commands.admin

import gg.scala.basics.plugin.commands.performed
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.lemon.util.QuickAccess.sendGlobalBroadcast
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.Color.translate
import org.bukkit.command.CommandSender
import java.util.concurrent.CompletableFuture

@AutoRegister
object AlertCommand : ScalaCommand()
{
    @CommandAlias("alert|globalbc")
    @CommandPermission("scbasics.command.alert")
    fun onAlert(sender: CommandSender, message: String): CompletableFuture<Void>
    {
        return sendGlobalBroadcast(translate("${CC.D_GRAY}[${CC.D_RED}Alert${CC.D_GRAY}] ${CC.RESET}$message"))
            .thenRun {
                sender performed "broadcasted global message"
            }
    }
}
