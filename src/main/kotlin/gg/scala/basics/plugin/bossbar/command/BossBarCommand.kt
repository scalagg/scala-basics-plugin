package gg.scala.basics.plugin.bossbar.command

import gg.scala.basics.plugin.bossbar.menu.ViewBossBarMenu
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Default
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer

@AutoRegister
@CommandAlias("managebossbar")
@CommandPermission("scbasics.command.bossbar")
object BossBarCommand : ScalaCommand()
{
    @Default
    fun onDefault(player: ScalaPlayer) = ViewBossBarMenu().openMenu(player.bukkit())
}
