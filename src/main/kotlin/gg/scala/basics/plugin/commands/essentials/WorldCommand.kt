package gg.scala.basics.plugin.commands.essentials

import gg.scala.commons.acf.CommandHelp
import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.Default
import gg.scala.commons.acf.annotation.Description
import gg.scala.commons.acf.annotation.HelpCommand
import gg.scala.commons.acf.annotation.Subcommand
import gg.scala.commons.annotations.commands.AssignPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import net.evilblock.cubed.util.CC
import org.bukkit.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.generator.BlockPopulator

import org.bukkit.generator.ChunkGenerator
import java.util.*

/**
 * @author GrowlyX
 * @since 5/27/2022
 */
@AutoRegister
@CommandAlias("world")
@CommandPermission("scbasics.command.world")
object WorldCommand : ScalaCommand()
{
    @Default
    @HelpCommand
    fun onHelp(help: CommandHelp)
    {
        help.showHelp()
    }

    @AssignPermission
    @Subcommand("load")
    @Description("Load in an existing world.")
    fun onLoadWorld(
        sender: CommandSender,
        world: String
    )
    {
        Bukkit.getServer()
            .createWorld(
                WorldCreator(world)
            )

        sender.sendMessage("${CC.GREEN}Loaded world $world.")
    }

    @AssignPermission
    @Subcommand("create")
    @Description("Create a new void world.")
    fun onCreateWorld(
        sender: CommandSender,
        world: String
    )
    {
        val existing = Bukkit
            .getWorld(world)

        if (existing != null)
            throw ConditionFailedException("That world already exists.")

        val newWorld = Bukkit.getServer()
            .createWorld(
                WorldCreator(world)
                    .generator(
                        VoidWorldGenerator
                    )
            )

        newWorld.setSpawnLocation(0, 62, 0)

        val block = newWorld
            .getBlockAt(0, 61, 0)

        block.type = Material.BEDROCK
        block.state.update()

        if (sender is Player)
        {
            sender.teleport(newWorld.spawnLocation)
        }

        sender.sendMessage("${CC.GREEN}Created new world $world.")
    }

    private object VoidWorldGenerator : ChunkGenerator()
    {
        @Deprecated(
            "Deprecated in Java",
            ReplaceWith("ByteArray(32768)")
        )
        override fun generate(
            world: World?, random: Random?, x: Int, z: Int
        ) = ByteArray(32768)

        override fun canSpawn(world: World?, x: Int, z: Int) = true
        override fun getDefaultPopulators(world: World?) = listOf<BlockPopulator>()
        override fun getFixedSpawnLocation(world: World?, random: Random?) = Location(world, 0.0, 62.0, 0.0)
    }
}
