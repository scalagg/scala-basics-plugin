package gg.scala.basics.plugin.commands.disguise

import dev.iiahmed.disguise.SkinAPI
import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.basics.plugin.disguise.DisguiseService
import gg.scala.basics.plugin.disguise.PlayerInfo
import gg.scala.basics.plugin.disguise.sessionserver.SessionServerResponse
import gg.scala.commons.acf.CommandHelp
import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.*
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.flavor.inject.Inject
import net.evilblock.cubed.serializers.Serializers
import net.evilblock.cubed.util.CC
import org.bukkit.command.ConsoleCommandSender
import java.io.File
import java.net.URL
import java.util.concurrent.CompletableFuture

/**
 * @author GrowlyX
 * @since 5/14/2023
 */
@AutoRegister
@CommandAlias("dga|disguiseadmin")
@CommandPermission("scbasics.command.disguiseadmin")
object DisguiseAdminCommand : ScalaCommand()
{
    @Inject
    lateinit var plugin: ScalaBasicsPlugin

    @Default
    @HelpCommand
    fun onHelp(help: CommandHelp) =
        help.showHelp()

    @Subcommand("import")
    @Description("Import disguises from a pre-defined list.")
    fun onImport(sender: ConsoleCommandSender)
    {
        val file = File(
            plugin.dataFolder,
            "fake_player_info.txt"
        )

        if (!file.exists())
        {
            throw ConditionFailedException("File not found")
        }

        file.readLines()
            .forEach { pair ->
                val playerInfo = Serializers
                    .gson.fromJson(
                        pair, PlayerInfo::class.java
                    )
                    ?: return@forEach run {
                        sender.sendMessage("${CC.RED}Failed to load for: $pair")
                    }

                DisguiseService
                    .disguiseProfileExists(playerInfo.id)
                    .thenCompose {
                        if (it)
                        {
                            sender.sendMessage("${CC.RED}Profile exists for: ${playerInfo.username}")
                            return@thenCompose CompletableFuture
                                .completedFuture(null)
                        }

                        val sessionServerResp = Serializers
                            .gson
                            .fromJson(
                                URL("https://sessionserver.mojang.com/session/minecraft/profile/${playerInfo.id}?unsigned=false")
                                    .readText(),
                                SessionServerResponse::class.java
                            )
                            ?: return@thenCompose CompletableFuture
                                .completedFuture(null)

                        DisguiseService
                            .saveDisguiseProfile(sessionServerResp)
                            .thenRun {
                                sender.sendMessage("${CC.GREEN}Saved profile for: ${playerInfo.username}")
                            }
                    }
            }
    }
}
