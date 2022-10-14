package gg.scala.basics.plugin.commands.customizer

import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.annotations.commands.customizer.CommandManagerCustomizer
import gg.scala.commons.command.ScalaCommandManager
import net.evilblock.cubed.util.CC
import org.bukkit.Bukkit
import org.bukkit.World

/**
 * @author GrowlyX
 * @since 10/13/2022
 */
object BasicPluginCommandCustomizer
{
    @CommandManagerCustomizer
    fun customize(manager: ScalaCommandManager)
    {
        manager.commandCompletions
            .registerAsyncCompletion("worlds") {
                Bukkit.getWorlds().map(World::getName)
            }

        manager.commandContexts
            .registerContext(World::class.java) {
                val arg = it.popFirstArg()

                Bukkit.getWorlds()
                    .firstOrNull { world ->
                        world.name.lowercase().startsWith(arg, true)
                    }
                    ?: throw ConditionFailedException(
                        "No world matching ${CC.YELLOW}$arg${CC.RED} could be found."
                    )
            }
    }
}
