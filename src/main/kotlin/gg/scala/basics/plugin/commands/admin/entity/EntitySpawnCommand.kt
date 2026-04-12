package gg.scala.basics.plugin.commands.admin.entity

import gg.scala.basics.plugin.commands.performed
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Optional
import gg.scala.commons.acf.annotation.Syntax
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import me.lucko.helper.utils.entityspawner.EntitySpawner
import net.evilblock.cubed.util.CC
import org.bukkit.entity.EntityType

@AutoRegister
object EntitySpawnCommand : ScalaCommand()
{
    @CommandAlias("spawnentity")
    @Syntax("<entity> [-n]")
    @CommandPermission("scbasics.command.spawnentity")
    fun onEntityClear(
        sender: ScalaPlayer, entity: EntityType, @Optional normalize: String?
    )
    {
        val location = if (normalize != null)
        {
            val temp = sender.bukkit().location
            temp.x = temp.x.toInt() + 0.500
            temp.z = temp.z.toInt() + 0.500
            temp
        } else
        {
            sender.bukkit().location
        }

        EntitySpawner.INSTANCE
            .spawn(
                location, entity.entityClass
            ) {

            }

        sender.sendMessage("${CC.SEC}Spawned a new ${CC.PRI}${
            entity.name.lowercase().capitalize().replace("_", " ")
        }${CC.SEC} entity at your location.${
            if (normalize != null) "${CC.GRAY} (location normalized)" else ""
        }")

        sender.bukkit() performed "spawned entity"
    }
}
