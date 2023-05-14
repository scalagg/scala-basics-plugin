package gg.scala.basics.plugin.commands.disguise

import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.basics.plugin.disguise.DisguiseService
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Conditions
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.flavor.inject.Inject
import gg.scala.lemon.player.wrapper.AsyncLemonPlayer

/**
 * @author GrowlyX
 * @since 2/5/2023
 */
@AutoRegister
object DisguiseCommands : ScalaCommand()
{
    @Inject
    lateinit var plugin: ScalaBasicsPlugin

    @CommandAlias("undisguise|ud|undis")
    @CommandPermission("scbasics.command.undisguise")
    fun onUnDisguise(player: ScalaPlayer) = DisguiseService
        .undisguise(player.bukkit())

    @Conditions("not-disguised")
    @CommandAlias("disguise|dis")
    @CommandPermission("scbasics.command.disguise")
    fun onDisguise(player: ScalaPlayer) =
        DisguiseService
            .disguiseWithRandomSelection(player.bukkit())

    @Conditions("not-disguised")
    @CommandAlias("disguisemanual")
    @CommandPermission("scbasics.command.disguise.manual")
    fun onDisguiseManual(player: ScalaPlayer, user: AsyncLemonPlayer) =
        user.validatePlayers(player.bukkit(), true) {
            DisguiseService
                .disguiseWithManualSelection(player.bukkit(), it.uniqueId)
                .join()
        }
}
