package gg.scala.basics.plugin.commands.customizer

import gg.scala.basics.plugin.chatfilter.ChatFilter
import gg.scala.basics.plugin.chatfilter.ChatFilterService
import gg.scala.basics.plugin.disguise.DisguiseService
import gg.scala.basics.plugin.profile.BasicsProfileService
import gg.scala.cache.uuid.ScalaStoreUuidCache
import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.agnostic.sync.ServerSync
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
        manager.commandConditions
            .addCondition("requires-disguise") {
                if (!DisguiseService.provider().isDisguised(it.issuer.player))
                    throw ConditionFailedException(
                        "You are not disguised! Disguise yourself using ${CC.BOLD}/disguise${CC.RED} to use this command!"
                    )
            }

        manager.commandConditions
            .addCondition("not-disguised") {
                if (DisguiseService.provider().isDisguised(it.issuer.player))
                    throw ConditionFailedException(
                        "You are already disguised! Use ${CC.BOLD}/undisguise${CC.RED} to use this command again!"
                    )
            }

        manager.commandConditions
            .addCondition("not-game-server") {
                if ("hub" !in ServerSync.getLocalGameServer().groups)
                    throw ConditionFailedException(
                        "You can only run this command in a lobby."
                    )
            }

        manager.commandCompletions
            .registerAsyncCompletion("ignoring") {
                val profile = BasicsProfileService.find(it.player)
                    ?: return@registerAsyncCompletion listOf()

                profile.ignoring.map(ScalaStoreUuidCache::username)
            }

        manager.commandCompletions
            .registerAsyncCompletion("worlds") {
                Bukkit.getWorlds().map(World::getName)
            }

        manager.commandCompletions
            .registerAsyncCompletion("chatfilters") {
                ChatFilterService.cached().chatFilters.map(ChatFilter::id)
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

        manager.commandContexts
            .registerContext(ChatFilter::class.java) {
                val arg = it.popFirstArg()

                ChatFilterService
                    .cached()
                    .findById(arg)
                    ?: throw ConditionFailedException(
                        "No chat filter matching ${CC.YELLOW}$arg${CC.RED} could be found."
                    )
            }
    }
}
