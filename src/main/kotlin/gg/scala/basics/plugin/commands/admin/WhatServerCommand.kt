package gg.scala.basics.plugin.commands.admin

import gg.scala.basics.plugin.service.LoginTimeService
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.lemon.Lemon
import net.evilblock.cubed.util.CC
import org.apache.commons.lang.time.DurationFormatUtils

/**
 * @author GrowlyX
 * @since 10/13/2022
 */
@AutoRegister
object WhatServerCommand : ScalaCommand()
{
    @CommandPermission("scbasics.command.whatserver")
    @CommandAlias("whatserveramion|whatserver|wsc")
    fun onWhatServer(player: ScalaPlayer)
    {
        player.sendMessage(
            "${CC.GREEN}You are on the server ${CC.PRI}${Lemon.instance.settings.id}${CC.GREEN}.",
            "${CC.SEC}You've been connected to this server for ${
                DurationFormatUtils.formatDurationWords(
                    System.currentTimeMillis() - LoginTimeService.loginTime(player.bukkit()), 
                    true, true
                )
            }."
        )
    }
}
