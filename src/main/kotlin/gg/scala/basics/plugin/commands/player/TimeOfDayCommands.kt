package gg.scala.basics.plugin.commands.player

import com.google.common.cache.CacheBuilder
import gg.scala.basics.plugin.settings.defaults.values.TimeOfDaySettingValue
import gg.scala.basics.plugin.timeofday.TimeOfDayService
import gg.scala.commons.acf.CommandIssuer
import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.acf.annotation.PreCommand
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import me.lucko.helper.Events
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.Color
import net.evilblock.cubed.util.Color.translate
import org.bukkit.Bukkit
import org.bukkit.event.block.BlockPlaceEvent
import java.util.*
import java.util.concurrent.TimeUnit

@AutoRegister
@CommandPermission("scbasics.player-commands")
object TimeOfDayCommands : ScalaCommand()
{
    private val commandCooldown = CacheBuilder.newBuilder()
        .expireAfterWrite(2L, TimeUnit.SECONDS)
        .build<UUID, Long>()!!

    fun preCommand(player: ScalaPlayer)
    {
        val cooldown = commandCooldown.getIfPresent(player.bukkit().uniqueId)

        if (cooldown != null)
        {
            throw ConditionFailedException(
                "Please wait ${"%.2f".format(((cooldown + 2000L) - System.currentTimeMillis()) / 1000.0)} more seconds before running this command again."
            )
        }

        commandCooldown.put(
            player.bukkit().uniqueId, System.currentTimeMillis()
        )
    }

    @CommandAlias("day")
    fun onDay(player: ScalaPlayer)
    {
        preCommand(player)
        TimeOfDayService.setToDForProfile(player.bukkit(), TimeOfDaySettingValue.Day)
        player.sendMessage("${CC.GREEN}Your time has been set to day!")
    }

    @CommandAlias("resettime")
    fun onOff(player: ScalaPlayer)
    {
        preCommand(player)
        TimeOfDayService.setToDForProfile(player.bukkit(), TimeOfDaySettingValue.Local)
        player.sendMessage("${CC.GREEN}Your time has been reset to the server's time!")
    }

    @CommandAlias("sunset")
    fun onSunset(player: ScalaPlayer)
    {
        preCommand(player)
        TimeOfDayService.setToDForProfile(player.bukkit(), TimeOfDaySettingValue.Sunset)
        player.sendMessage("${CC.GREEN}Your time has been set to sunset!")
    }

    @CommandAlias("night")
    fun onNight(player: ScalaPlayer)
    {
        preCommand(player)
        TimeOfDayService.setToDForProfile(player.bukkit(), TimeOfDaySettingValue.Night)
        player.sendMessage("${CC.GREEN}Your time has been set to night!")
    }
}
