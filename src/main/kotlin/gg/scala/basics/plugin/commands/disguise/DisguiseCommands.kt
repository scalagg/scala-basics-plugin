package gg.scala.basics.plugin.commands.disguise

import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.basics.plugin.disguise.DisguiseService
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandCompletion
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Conditions
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.flavor.inject.Inject
import gg.scala.lemon.player.wrapper.AsyncLemonPlayer
import gg.scala.lemon.util.QuickAccess
import gg.scala.lemon.util.QuickAccess.username
import net.evilblock.cubed.util.CC
import java.util.*

/**
 * @author GrowlyX
 * @since 2/5/2023
 */
@AutoRegister
object DisguiseCommands : ScalaCommand()
{
    @Inject
    lateinit var plugin: ScalaBasicsPlugin

    @CommandAlias("checkdisguise|checkdis")
    @CommandCompletion("@players")
    @CommandPermission("scbasics.command.checkdisguise")
    fun onCheckDisguise(player: ScalaPlayer, target: AsyncLemonPlayer) =
        target.validatePlayers(player.bukkit(), true) {
            DisguiseService
                .getUsedProfile(it.uniqueId)
                .thenAccept { resp ->
                    if (resp == null)
                    {
                        player.sendMessage(
                            "${CC.RED}This user is not disguised!"
                        )
                        return@thenAccept
                    }

                    val playerId = UUID.fromString(resp)
                    val server = QuickAccess.server(playerId)
                        .join()

                    player.sendMessage(
                        "${CC.GREEN}${playerId.username()} is disguised as ${it.name} and is ${
                            if (server == null) "offline" else "playing on ${server.id} ${CC.GRAY}(${server.region.display})${CC.GREEN}"
                        }."
                    )
                }
        }

    @CommandAlias("undisguise|ud|undis|unnick")
    @CommandPermission("scbasics.command.undisguise")
    fun onUnDisguise(player: ScalaPlayer) = DisguiseService
        .undisguise(player.bukkit())

    @Conditions("not-disguised|not-game-server")
    @CommandAlias("disguise|dis|nick")
    @CommandPermission("scbasics.command.disguise")
    fun onDisguise(player: ScalaPlayer) =
        DisguiseService
            .disguiseWithRandomSelection(player.bukkit())

    @CommandAlias("disguisemanual|manualnick")
    @Conditions("not-disguised|not-game-server")
    @CommandPermission("scbasics.command.disguise.manual")
    fun onDisguiseManual(player: ScalaPlayer, user: AsyncLemonPlayer) =
        user.validatePlayers(player.bukkit(), true) {
            DisguiseService
                .disguiseWithManualSelection(player.bukkit(), it.uniqueId)
                .join()
        }
}
