package gg.scala.basics.plugin.commands.player

import gg.scala.basics.plugin.settings.SettingMenu
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer

/**
 * @author GrowlyX
 * @since 10/13/2022
 */
@AutoRegister
object SettingsCommand : ScalaCommand()
{
    @CommandPermission("scbasics.player-commands")
    @CommandAlias("settings|prefs|preferences|mysettings|myprefs")
    fun onPing(player: ScalaPlayer)
    {
        SettingMenu().openMenu(player.bukkit())
    }
}
