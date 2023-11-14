package gg.scala.basics.plugin.commands.disguise

import dev.iiahmed.disguise.Disguise
import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.basics.plugin.disguise.DisguiseService
import gg.scala.basics.plugin.disguise.PlayerDataPair
import gg.scala.basics.plugin.disguise.sessionserver.SessionServerResponse
import gg.scala.commons.acf.CommandHelp
import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.*
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.flavor.inject.Inject
import net.evilblock.cubed.ScalaCommonsSpigot
import net.evilblock.cubed.serializers.Serializers
import net.evilblock.cubed.util.CC
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.EntityType
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

    @Subcommand("iamnowafucking")
    @Description("I am now a fucking... PIG!")
    fun onIAmNowAFucking(player: ScalaPlayer, type: EntityType)
    {
        DisguiseService.provider().disguise(
            player.bukkit(),
            Disguise
                .builder()
                .setEntityType(type)
                .build()
        )

        player.sendMessage(
            "${CC.GREEN}You're now a fucking ${type.name}!"
        )
    }

    @Subcommand("iamnolongerafucking")
    @Description("I am no longer a fucking pig... :(")
    fun onIAmNoLongerAFucking(player: ScalaPlayer)
    {
        DisguiseService.provider().undisguise(player.bukkit())
        player.sendMessage("${CC.RED}Your entity disguise was reset.")
    }

    @Subcommand("export")
    fun onExport(sender: ConsoleCommandSender)
    {
        File(plugin.dataFolder, "export.json")
            .apply {
                if (!exists())
                {
                    plugin.dataFolder.mkdirs()
                    createNewFile()
                }
            }
            .writeText(
                Serializers.gson.toJson(
                    ScalaCommonsSpigot.instance.kvConnection
                        .sync()
                        .hgetall("disguise:profiles")
                        .mapValues {
                            Serializers.gson.fromJson(it.value, SessionServerResponse::class.java)
                        }
                        .mapValues { it.value.name }
                        .values
                )
            )

     sender.sendMessage("ddone!")
    }

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
                val playerDataPair = Serializers
                    .gson.fromJson(
                        pair, PlayerDataPair::class.java
                    )
                    ?: return@forEach run {
                        sender.sendMessage("${CC.RED}Failed to load for: $pair")
                    }

                DisguiseService
                    .disguiseProfileExists(playerDataPair.id)
                    .thenCompose {
                        if (it)
                        {
                            sender.sendMessage("${CC.RED}Profile exists for: ${playerDataPair.username}")
                            return@thenCompose CompletableFuture
                                .completedFuture(null)
                        }

                        val sessionServerResp = Serializers
                            .gson
                            .fromJson(
                                URL("https://sessionserver.mojang.com/session/minecraft/profile/${playerDataPair.id}?unsigned=false")
                                    .readText(),
                                SessionServerResponse::class.java
                            )
                            ?: return@thenCompose CompletableFuture
                                .completedFuture(null)

                        DisguiseService
                            .saveDisguiseProfile(sessionServerResp)
                            .thenRun {
                                sender.sendMessage("${CC.GREEN}Saved profile for: ${playerDataPair.username}")
                            }
                    }
            }
    }
}
