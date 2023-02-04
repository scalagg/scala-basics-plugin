package gg.scala.basics.plugin.commands.player

import gg.scala.basics.plugin.commands.cachedBasicProfile
import gg.scala.basics.plugin.profile.BasicsProfileService
import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandCompletion
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.lemon.player.wrapper.AsyncLemonPlayer
import net.evilblock.cubed.util.CC
import java.util.concurrent.CompletableFuture

/**
 * @author GrowlyX
 * @since 10/13/2022
 */
@AutoRegister
object UnIgnoreCommand : ScalaCommand()
{
    @CommandCompletion("@ignoring")
    @CommandAlias("unignore|unblock")
    @CommandPermission("scbasics.player-commands")
    fun onUnIgnore(player: ScalaPlayer, target: AsyncLemonPlayer): CompletableFuture<Void>
    {
        val profile = cachedBasicProfile(player)

        return target
            .validatePlayers(player.bukkit(), true) {
                if (!profile.ignoring.contains(it.uniqueId))
                {
                    throw ConditionFailedException(
                        "You're not ignoring ${CC.BOLD}${it.name}${CC.RED}!"
                    )
                }

                profile.ignoring.remove(it.uniqueId)
                profile.save().join()

                player.sendMessage(
                    "${CC.SEC}You've removed ${CC.PRI}${it.name}${CC.SEC} from your ignore-list."
                )
            }
    }
}
