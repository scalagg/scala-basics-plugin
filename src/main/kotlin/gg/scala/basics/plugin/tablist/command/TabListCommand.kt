package gg.scala.basics.plugin.tablist.command

import gg.scala.basics.plugin.tablist.menu.ViewTabListMenu
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Default
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer

@AutoRegister
@CommandAlias("managetablist|tablist|tabconfig|tablistconfig|tabconf|tab")
@CommandPermission("scbasics.command.tablist")
object TabListCommand : ScalaCommand()
{
    @Default
    fun onDefault(player: ScalaPlayer) = ViewTabListMenu().openMenu(player.bukkit())
}
