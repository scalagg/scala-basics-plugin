package gg.scala.basics.plugin.commands.player

import gg.scala.basics.plugin.commands.cachedBasicProfile
import gg.scala.basics.plugin.ignorelist.IgnoreListFormatter
import gg.scala.basics.plugin.profile.BasicsProfileService
import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandCompletion
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Default
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.lemon.player.wrapper.AsyncLemonPlayer
import gg.scala.lemon.util.QuickAccess
import net.evilblock.cubed.util.CC
import java.util.concurrent.CompletableFuture

/**
 * @author GrowlyX
 * @since 10/13/2022
 */
@AutoRegister
@CommandPermission("scbasics.player-commands")
@CommandAlias("ignore|block|ignoring|blocked|ignorelist|il")
object IgnoreCommand : ScalaCommand()
{
    @Default
    @CommandCompletion("@players")
    fun onIgnore(
        player: ScalaPlayer, @Default("1") page: Int
    )
    {
        val profile = cachedBasicProfile(player)

        if (profile.ignoring.isEmpty())
        {
            throw ConditionFailedException(
                "You are not ignoring anyone! Use ${CC.BOLD}/ignore <username>${CC.RED} to add a user to your ignore list!"
            )
        }

        IgnoreListFormatter
            .display(
                player.bukkit(),
                profile.ignoring.toList(),
                page,
                command = "ignore %s"
            )
    }

    @Default
    @CommandCompletion("@players")
    fun onIgnore(
        player: ScalaPlayer, lazyPlayer: AsyncLemonPlayer
    ): CompletableFuture<Void>
    {
        val profile = cachedBasicProfile(player)

        return lazyPlayer
            .validatePlayers(player.bukkit(), false) {
                val rank = QuickAccess
                    .computeRank(it.uniqueId)
                    .join()
                    ?: throw ConditionFailedException(
                        "You're unable to ignore this user!"
                    )

                if (
                    rank.getCompoundedPermissions()
                        .contains("scstaff.staff-member")
                )
                {
                    throw ConditionFailedException(
                        "You're unable to ignore staff members!"
                    )
                }

                if (profile.ignoring.contains(it.uniqueId))
                {
                    throw ConditionFailedException(
                        "You're already ignoring ${CC.YELLOW}${it.name}${CC.RED}!"
                    )
                }

                profile.ignoring.add(it.uniqueId)
                profile.save().join()

                player.sendMessage(
                    "${CC.SEC}You're now ignoring ${rank.color}${it.name}${CC.SEC}!"
                )
            }
    }
}
