package gg.scala.basics.plugin.commands.essentials

import gg.scala.basics.plugin.commands.performed
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Flags
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import net.evilblock.cubed.util.CC
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
@AutoRegister
object MoreCommand : ScalaCommand()
{
    @CommandAlias("more|mi|stack|stackitem|si")
    @CommandPermission("scbasics.command.more")
    fun onMore(
        @Flags("itemheld") player: Player
    )
    {
        player.itemInHand.amount = 64
        player.updateInventory()

        player.sendMessage(
            "${CC.GREEN}You've stacked the item in your hand."
        )

        player performed "stacked item in hand"
    }
}
