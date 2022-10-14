package gg.scala.basics.plugin.commands.admin.entity

import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Optional
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.math.Numbers
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/13/2022
 */
@AutoRegister
object EntityCountCommand : ScalaCommand()
{
    @CommandAlias("entitycount|countents|countentities|entities")
    @CommandPermission("scbasics.command.entitycount")
    fun onEntityCount(sender: CommandSender, @Optional world: World?)
    {
        if (sender !is Player && world == null)
        {
            throw ConditionFailedException(
                "You must specify a world as you're performing this command through console!"
            )
        }

        val worldOptional = world ?: (sender as Player).world
        sender.sendMessage(
            "${CC.SEC}There are ${CC.PRI}${
                Numbers.format(worldOptional.entities.size)
            }${CC.SEC} entities in the world ${CC.PRI}${worldOptional.name}${CC.SEC}."
        )
    }
}
