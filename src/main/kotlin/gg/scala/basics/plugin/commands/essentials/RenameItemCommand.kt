package gg.scala.basics.plugin.commands.essentials

import gg.scala.basics.plugin.commands.performed
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Flags
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.Color.translate
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/13/2022
 */
@AutoRegister
object RenameItemCommand : ScalaCommand()
{
    @CommandAlias("rename|renameitem")
    @CommandPermission("scbasics.command.rename")
    fun onRename(
        @Flags("itemheld") sender: Player, newName: String
    )
    {
        val oldName = sender.itemInHand.itemMeta.displayName

        val itemInHand = ItemBuilder
            .copyOf(sender.itemInHand)
            .name(translate(newName))
            .build()

        sender.itemInHand = itemInHand
        sender.sendMessage(
            "${CC.SEC}You've renamed the item in your hand to ${CC.RESET}${translate(newName)}${CC.SEC} from ${CC.RESET}$oldName${CC.SEC}."
        )

        sender performed "renamed item in hand to $newName"
    }
}
