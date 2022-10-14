package gg.scala.basics.plugin.commands.admin.entity

import gg.scala.basics.plugin.commands.performed
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import me.lucko.helper.utils.entityspawner.EntitySpawner
import net.evilblock.cubed.util.CC
import org.bukkit.entity.EntityType

/**
 * @author GrowlyX
 * @since 10/13/2022
 */
@AutoRegister
object EntitySpawnCommand : ScalaCommand()
{
    @CommandAlias("spawnentity")
    @CommandPermission("scbasics.command.spawnentity")
    fun onEntityClear(sender: ScalaPlayer, type: EntityType)
    {
        EntitySpawner.INSTANCE
            .spawn(
                sender.bukkit().location, type.entityClass
            ) {

            }

        sender.sendMessage("${CC.SEC}Spawned ${CC.PRI}${
            type.name.lowercase().capitalize().replace("_", " ")
        }${CC.SEC} entity at your location.")

        sender.bukkit() performed "spawned entity"
    }
}
