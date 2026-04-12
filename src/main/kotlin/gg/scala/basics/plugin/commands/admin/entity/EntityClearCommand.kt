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

@AutoRegister
object EntityClearCommand : ScalaCommand()
{
    @CommandAlias("clearentities|clearents")
    @CommandPermission("scbasics.command.clearentities")
    fun onEntityClear(sender: CommandSender, @Optional world: World?)
    {
        if (sender !is Player && world == null)
        {
            throw ConditionFailedException(
                "You must specify a world as you're performing this command through console!"
            )
        }

        val worldOptional = world ?: (sender as Player).world
        val removed = worldOptional.entities
            .filterNot { it is Player }
            .onEach {
                it.remove()
            }
            .count()

        sender.sendMessage("${CC.SEC}Removed ${CC.PRI}${
            Numbers.format(removed)
        }${CC.SEC} non-player entities from the world ${CC.PRI}${worldOptional.name}${CC.SEC}.")
    }
}
