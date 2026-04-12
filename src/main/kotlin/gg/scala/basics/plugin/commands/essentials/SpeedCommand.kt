package gg.scala.basics.plugin.commands.essentials

import gg.scala.basics.plugin.commands.performed
import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import net.evilblock.cubed.util.CC
import org.bukkit.entity.Player

@AutoRegister
object SpeedCommand : ScalaCommand()
{
    @CommandAlias("speed")
    @CommandPermission("scbasics.command.speed")
    fun onSpeed(player: Player, speed: Int)
    {
        if (speed > 10 || speed <= 0)
        {
            throw ConditionFailedException("Please enter a number from 0-10.")
        }

        val translated = translate(
            speed, player.isFlying
        )

        if (player.isFlying)
        {
            player.flySpeed = translated
        } else
        {
            player.walkSpeed = translated
        }

        player performed "set speed to $speed"

        player.sendMessage("${CC.GREEN}You've set your ${
            if (player.isFlying) "fly" else "walk"
        } speed to ${CC.WHITE}${speed}${CC.GREEN}.")
    }

    private fun translate(speed: Int, flying: Boolean): Float
    {
        val defaultSpeed = if (flying)
            0.1f else 0.2f

        val maxSpeed = 1.0f

        if (speed < 1.0f)
            return defaultSpeed * speed

        val ratio = (speed - 1.0f) / 9.0f * (maxSpeed - defaultSpeed)

        return ratio + defaultSpeed
    }
}
