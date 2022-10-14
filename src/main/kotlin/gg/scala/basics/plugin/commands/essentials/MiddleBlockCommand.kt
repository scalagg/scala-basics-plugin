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
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.util.BlockIterator

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
        val block = getTargetBlock(player.bukkit())

        if (block.type == Material.AIR)
        {
            throw ConditionFailedException("You must be staring at a block!")
        }

        val temp = block.location
        temp.x = temp.x.toInt() + 0.500
        temp.y = temp.y.toInt() + 1.000
        temp.z = temp.z.toInt() + 0.500

        temp.pitch = player.bukkit().location.pitch
        temp.yaw = player.bukkit().location.yaw

        player.teleport(temp)

        player.sendMessage(
            "${CC.GREEN}Moved you to the middle of the block."
        )

        player performed "moved to middle of block"
    }

    private fun getTargetBlock(player: Player): Block
    {
        val iter = BlockIterator(player, 5)
        var lastBlock = iter.next()

        while (iter.hasNext())
        {
            lastBlock = iter.next()

            if (lastBlock.type === Material.AIR)
            {
                continue
            }

            break
        }

        return lastBlock
    }
}
