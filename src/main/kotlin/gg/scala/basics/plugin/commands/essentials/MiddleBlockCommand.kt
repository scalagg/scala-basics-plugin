package gg.scala.basics.plugin.commands.essentials

import gg.scala.basics.plugin.commands.performed
import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import net.evilblock.cubed.util.CC
import org.bukkit.Material

/**
 * @author GrowlyX
 * @since 10/13/2022
 */
@AutoRegister
object MiddleBlockCommand : ScalaCommand()
{
    @CommandAlias("middleblock|middle|mb")
    @CommandPermission("scbasics.command.middleblock")
    fun onDefault(player: ScalaPlayer)
    {
        val temp = player.bukkit().eyeLocation

        if (temp.block == null || temp.block.type == Material.AIR)
        {
            throw ConditionFailedException("You must be staring at a block!")
        }

        temp.x = temp.x.toInt() + 0.500
        temp.y = temp.y.toInt() + 2.000
        temp.z = temp.z.toInt() + 0.500

        player.teleport(temp)

        player.sendMessage(
            "${CC.GREEN}Moved you to the middle of the block."
        )

        player performed "moved to middle of block"
    }
}
