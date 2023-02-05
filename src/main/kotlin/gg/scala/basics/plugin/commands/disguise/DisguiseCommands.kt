package gg.scala.basics.plugin.commands.disguise

import dev.iiahmed.disguise.Disguise
import dev.iiahmed.disguise.DisguiseResponse
import dev.iiahmed.disguise.SkinAPI
import gg.scala.basics.plugin.disguise.DisguiseService
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.lemon.player.wrapper.AsyncLemonPlayer
import org.bukkit.entity.EntityType
import java.util.concurrent.CompletableFuture

/**
 * @author GrowlyX
 * @since 2/5/2023
 */
@AutoRegister
object DisguiseCommands : ScalaCommand()
{
    @CommandAlias("disguise")
    @CommandPermission("scbasics.command.disguise")
    fun onDisguise(player: ScalaPlayer, target: AsyncLemonPlayer): CompletableFuture<Void>
    {
        return target
            .validatePlayers(player.bukkit(), true) {
                val disguise = Disguise.builder()
                    .setName(it.name, false)
                    .setSkin(SkinAPI.MOJANG_UUID, it.uniqueId.toString())
                    .setEntityType(EntityType.ZOMBIE)
                    .build()

                val response = DisguiseService
                    .provider()
                    .disguise(player.bukkit(), disguise)

                when (response)
                {
                    DisguiseResponse.SUCCESS -> player.sendMessage("Disguise is successful.")
                    DisguiseResponse.FAIL_NAME_ALREADY_ONLINE -> player.sendMessage("There's already an online player with that nickname.")
                    else -> player.sendMessage("Disguise is unsuccessful with the reason $response")
                }
            }
    }
}
