package gg.scala.basics.plugin.commands.essentials

import gg.scala.basics.plugin.commands.bukkit
import gg.scala.basics.plugin.commands.performed
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandCompletion
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Optional
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.lemon.player.LemonPlayer
import net.evilblock.cubed.util.CC
import org.bukkit.GameMode

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
@AutoRegister
object GamemodeCommand : ScalaCommand()
{
    @CommandAlias("gmc")
    @CommandCompletion("@players")
    @CommandPermission("scbasics.command.gmc")
    fun onGmc(
        player: ScalaPlayer, @Optional target: LemonPlayer?
    )
    {
        performGamemodeSwitch(
            player = player,
            target = target,
            gameMode = GameMode.CREATIVE
        )
    }

    @CommandAlias("gms")
    @CommandCompletion("@players")
    @CommandPermission("scbasics.command.gms")
    fun onGms(
        player: ScalaPlayer, @Optional target: LemonPlayer?
    )
    {
        performGamemodeSwitch(
            player = player,
            target = target,
            gameMode = GameMode.SURVIVAL
        )
    }

    @CommandAlias("gmsp")
    @CommandCompletion("@players")
    @CommandPermission("scbasics.command.gmsp")
    fun onGmsp(
        player: ScalaPlayer, @Optional target: LemonPlayer?
    )
    {
        performGamemodeSwitch(
            player = player,
            target = target,
            gameMode = GameMode.SPECTATOR
        )
    }

    @CommandAlias("gma")
    @CommandCompletion("@players")
    @CommandPermission("scbasics.command.gma")
    fun onGma(
        player: ScalaPlayer, @Optional target: LemonPlayer?
    )
    {
        performGamemodeSwitch(
            player = player,
            target = target,
            gameMode = GameMode.ADVENTURE
        )
    }

    private fun performGamemodeSwitch(
        player: ScalaPlayer, target: LemonPlayer?, gameMode: GameMode
    )
    {
        if (target != null)
        {
            player.sendMessage(
                "${CC.SEC}You've set ${target.getOriginalColoredName()}'s${CC.SEC} gamemode to ${CC.GREEN}${gameMode.name.lowercase()}${CC.SEC}."
            )

            target.bukkit().gameMode = gameMode

            player performed "set ${target.name}'s gamemode to ${gameMode.name.lowercase()}"
            return
        }

        player.sendMessage(
            "${CC.SEC}You've set your gamemode to ${CC.GREEN}${gameMode.name.lowercase()}${CC.SEC}."
        )

        player.bukkit().gameMode = gameMode

        player performed "set gamemode to ${gameMode.name.lowercase()}"
    }
}
