package gg.scala.basics.plugin.commands.admin

import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import net.evilblock.cubed.util.CC
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import java.nio.file.Files
import java.nio.file.StandardOpenOption

/**
 * @author GrowlyX
 * @since 3/30/2023
 */
@AutoRegister
object SetMaxPlayersCommand : ScalaCommand()
{
    @CommandAlias("setmaxplayers|setplayercap")
    @CommandPermission("scbasics.command.setmaxplayers")
    fun onSetMaxPlayers(sender: CommandSender, slots: Int)
    {
        if (slots < 1)
        {
            throw ConditionFailedException("Please choose a number greater than ${CC.YELLOW}1${CC.RED}.")
        }

        val previous = Bukkit.getMaxPlayers()
        updateServerPlayerSlots(slots)
            .onFailure {
                sender.sendMessage("${CC.RED}We were unable to update the max player slots!")
            }
            .onSuccess {
                sender.sendMessage("${CC.GREEN}The max player slots has been set to ${CC.WHITE}$slots${CC.GREEN} from ${CC.WHITE}$slots${CC.GREEN}!")
            }
    }

    fun updateServerPlayerSlots(slots: Int) =
        runCatching {
            val invoke = Class
                .forName(Bukkit.getServer().javaClass.getPackage().name + ".CraftServer")
                .getDeclaredMethod("getHandle", *arrayOfNulls(0))
                .invoke(Bukkit.getServer())

            val declaredField = invoke.javaClass.superclass
                .getDeclaredField("maxPlayers")

            declaredField.isAccessible = true
            declaredField.set(invoke, slots)

            val resolve = Bukkit.getWorldContainer()
                .toPath()
                .resolve("server.properties")

            val allLines = Files.readAllLines(resolve)

            allLines.removeIf {
                it.startsWith("max-players")
            }

            allLines.add("max-players=$slots")

            Files.write(
                resolve, allLines,
                StandardOpenOption.TRUNCATE_EXISTING
            )
        }
}
