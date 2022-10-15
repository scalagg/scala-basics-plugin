package gg.scala.basics.plugin.commands.essentials

import gg.scala.basics.plugin.commands.performed
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandCompletion
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.lemon.player.LemonPlayer
import net.evilblock.cubed.util.CC
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 9/9/2022
 */
@AutoRegister
object MassayCommand : ScalaCommand()
{
    enum class SudoType(
        val lambda: (Player, String) -> Unit
    )
    {
        Chat({ player, content ->
            player.chat(content)
        }),
        Command({ player, content ->
            player.chat("/$content")
        })
    }

    @CommandAlias("masssay|sudoall")
    @CommandPermission("scbasics.command.sudoall")
    fun onMassay(
        sender: CommandSender, type: SudoType, content: String
    )
    {
        for (other in Bukkit.getOnlinePlayers())
        {
            type.lambda(other, content)
        }

        sender.sendMessage("${CC.GREEN}Sudo'd everyone to ${type.name.lowercase()}: ${CC.WHITE}$content")
        sender performed "sudo'd all to ${type.name.lowercase()} '$content'"
    }

    @CommandAlias("sudo")
    @CommandCompletion("@players")
    @CommandPermission("scbasics.command.sudo")
    fun onSudo(
        sender: CommandSender, target: LemonPlayer, type: SudoType, content: String
    )
    {
        type.lambda(target.bukkitPlayer!!, content)

        sender.sendMessage(
            "${CC.GREEN}Sudo'd ${CC.WHITE}${target.name}${CC.GREEN} to ${type.name.lowercase()}: ${CC.WHITE}$content"
        )

        sender performed "sudo'd ${target.name} to ${type.name.lowercase()} '$content'"
    }
}
