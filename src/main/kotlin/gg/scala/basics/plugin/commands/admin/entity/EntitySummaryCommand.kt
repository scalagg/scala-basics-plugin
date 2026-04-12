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
object EntitySummaryCommand : ScalaCommand()
{
    @CommandAlias("entitysummary|dumpentities")
    @CommandPermission("scbasics.command.entitysummary")
    fun onEntitySummary(sender: CommandSender, @Optional world: World?)
    {
        if (sender !is Player && world == null)
        {
            throw ConditionFailedException(
                "You must specify a world as you're performing this command through console!"
            )
        }

        val worldOptional = world ?: (sender as Player).world
        val types = worldOptional.entities.map { it.type }.toSet()

        sender.sendMessage("${CC.SEC}Total entity count: ${CC.PRI}${
            Numbers.format(worldOptional.entities.size)
        }")
        sender.sendMessage("")
        sender.sendMessage("${CC.SEC}Entity types:")

        for (type in types)
        {
            val entities = Numbers.format(
                worldOptional.entities.count { it.type == type }
            )

            sender.sendMessage(
                " ${CC.SEC}${type.name.lowercase().capitalize()}: ${CC.PRI}$entities"
            )
        }
    }
}
