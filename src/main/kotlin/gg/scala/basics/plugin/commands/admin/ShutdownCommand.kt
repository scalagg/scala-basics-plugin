package gg.scala.basics.plugin.commands.admin

import gg.scala.basics.plugin.shutdown.ShutdownService
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.acf.CommandHelp
import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.*
import gg.scala.commons.annotations.commands.AssignPermission
import gg.scala.commons.annotations.commands.AutoRegister
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.time.Duration
import net.evilblock.cubed.util.time.TimeUtil
import org.bukkit.entity.Player

/**
 * @author puugz
 * @since 27/08/2021 20:02
 */
@AutoRegister
@CommandAlias("shutdown|shutdowntimer")
@CommandPermission("scbasics.command.shutdown")
object ShutdownCommand : ScalaCommand()
{
    @Default
    @HelpCommand
    fun onHelp(help: CommandHelp)
    {
        help.showHelp()
    }

    @AssignPermission
    @Subcommand("initiate|start")
    @Description("Initiate a server shutdown.")
    fun onInitiate(player: Player, @Name("time") time: Duration)
    {
        if (time.isPermanent())
        {
            throw ConditionFailedException("The duration you provided is too long.")
        }

        val seconds = try
        {
            (time.get() / 1000).toInt()
        } catch (exception: Exception)
        {
            throw ConditionFailedException("You provided an invalid duration, please try again.")
        }

        player.sendMessage("${CC.GREEN}The shutdown has been initiated.")

        ShutdownService.initiateShutdown(seconds)
    }

    @AssignPermission
    @Subcommand("status")
    @Description("View the status of the current shutdown.")
    fun onStatus(player: Player)
    {
        val shutdown = ShutdownService.shutdownRunnable

        if (shutdown != null)
        {
            player.sendMessage(
                "${CC.RED}The server is scheduled to shutdown in ${CC.YELLOW}${
                    TimeUtil.formatIntoDetailedString(
                        shutdown.seconds
                    )
                }${CC.RED}."
            )
        } else
        {
            player.sendMessage("${CC.RED}There is currently no scheduled shutdown.")
        }
    }

    @AssignPermission
    @Subcommand("cancel|stop")
    @Description("Cancel the current shutdown.")
    fun onCancel(player: Player)
    {
        ShutdownService.cancelShutdown()
    }
}
