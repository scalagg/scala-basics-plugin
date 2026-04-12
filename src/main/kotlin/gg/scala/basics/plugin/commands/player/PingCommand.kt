package gg.scala.basics.plugin.commands.player

import gg.scala.basics.plugin.commands.bukkit
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandCompletion
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Default
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.lemon.player.LemonPlayer
import gg.scala.lemon.util.QuickAccess
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.math.Numbers

@AutoRegister
@CommandAlias("ping|latency")
@CommandPermission("scbasics.player-commands")
object PingCommand : ScalaCommand()
{
    @Default
    fun onPing(player: ScalaPlayer)
    {
        val ping = Numbers
            .format(
                player.bukkit().ping
            )

        player.sendMessage(
            "${CC.GREEN}You have a ping of ${CC.GOLD}${ping}ms${CC.GREEN}."
        )
    }

    @Default
    @CommandCompletion("@players")
    fun onPing(player: ScalaPlayer, target: LemonPlayer)
    {
        val ping = Numbers
            .format(
                target.bukkit().ping
            )

        player.sendMessage(
            "${QuickAccess.coloredName(target.bukkit(), true)}${CC.GREEN} has a ping of ${CC.GOLD}${ping}ms${CC.GREEN}."
        )
    }
}
