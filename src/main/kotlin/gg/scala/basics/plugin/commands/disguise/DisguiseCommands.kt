package gg.scala.basics.plugin.commands.disguise

import dev.iiahmed.disguise.Disguise
import dev.iiahmed.disguise.DisguiseResponse
import dev.iiahmed.disguise.SkinAPI
import gg.scala.basics.plugin.disguise.DisguiseService
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Conditions
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.lemon.handler.PlayerHandler
import gg.scala.lemon.handler.RankHandler
import gg.scala.lemon.player.wrapper.AsyncLemonPlayer
import gg.scala.lemon.util.QuickAccess
import net.evilblock.cubed.nametag.NametagHandler
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Tasks
import org.bukkit.entity.EntityType
import java.util.concurrent.CompletableFuture

/**
 * @author GrowlyX
 * @since 2/5/2023
 */
@AutoRegister
object DisguiseCommands : ScalaCommand()
{
    @Conditions("requires-disguise")
    @CommandAlias("undisguise|ud|undis")
    @CommandPermission("scbasics.command.undisguise")
    fun onUnDisguise(player: ScalaPlayer)
    {
        DisguiseService.provider().undisguise(player.bukkit())

        PlayerHandler.findPlayer(player.uniqueId)
            .ifPresent { lemonPlayer ->
                lemonPlayer.disguiseRankUniqueId = null
            }

        QuickAccess.reloadPlayer(player.bukkit().uniqueId, false)

        player.sendMessage("${CC.GREEN}You've been undisguised!")
    }

    @Conditions("not-disguised")
    @CommandAlias("disguise|dis")
    @CommandPermission("scbasics.command.disguise")
    fun onDisguise(player: ScalaPlayer, target: AsyncLemonPlayer): CompletableFuture<Void>
    {
        return target
            .validatePlayers(player.bukkit(), true) {
                val disguise = Disguise.builder()
                    .setName(it.name, false)
                    .setSkin(SkinAPI.MOJANG_UUID, it.uniqueId.toString())
                    .setEntityType(EntityType.PLAYER)
                    .build()

                Tasks.sync {
                    val response = DisguiseService
                        .provider()
                        .disguise(player.bukkit(), disguise)

                    when (response)
                    {
                        DisguiseResponse.SUCCESS ->
                        {
                            player.sendMessage("Disguise is successful.")

                            PlayerHandler.findPlayer(player.uniqueId)
                                .ifPresent { lemonPlayer ->
                                    lemonPlayer.disguiseRankUniqueId = RankHandler
                                        .getDefaultRank().uuid
                                }

                            QuickAccess.reloadPlayer(player.bukkit().uniqueId, false)
                        }
                        DisguiseResponse.FAIL_NAME_ALREADY_ONLINE -> player.sendMessage("There's already an online player with that nickname.")
                        else -> player.sendMessage("Disguise is unsuccessful with the reason $response")
                    }
                }
            }
    }
}
