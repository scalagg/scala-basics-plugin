package gg.scala.basics.plugin.commands.disguise

import dev.iiahmed.disguise.Disguise
import dev.iiahmed.disguise.DisguiseResponse
import dev.iiahmed.disguise.SkinAPI
import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.basics.plugin.disguise.DisguiseService
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Conditions
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.flavor.inject.Inject
import gg.scala.lemon.command.annotations.NoManualSelection
import gg.scala.lemon.handler.PlayerHandler
import gg.scala.lemon.handler.RankHandler
import gg.scala.lemon.player.wrapper.AsyncLemonPlayer
import gg.scala.lemon.util.QuickAccess
import net.evilblock.cubed.nametag.NametagHandler
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Tasks
import org.bukkit.entity.EntityType
import org.bukkit.metadata.FixedMetadataValue
import java.util.concurrent.CompletableFuture

/**
 * @author GrowlyX
 * @since 2/5/2023
 */
@AutoRegister
object DisguiseCommands : ScalaCommand()
{
    @Inject
    lateinit var plugin: ScalaBasicsPlugin

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

        player.bukkit().removeMetadata("disguised", plugin)
        QuickAccess.reloadPlayer(player.bukkit().uniqueId, false)

        player.sendMessage("${CC.GREEN}You've been undisguised!")
    }

    @Conditions("not-disguised")
    @CommandAlias("disguise|dis")
    @CommandPermission("scbasics.command.disguise")
    fun onDisguise(player: ScalaPlayer, name: String)
    {
        DisguiseService.disguise(player.bukkit(), name)
    }
}
