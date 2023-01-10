package gg.scala.basics.plugin.commands.player

import com.google.common.cache.CacheBuilder
import gg.scala.basics.plugin.settings.defaults.values.TimeOfDaySettingValue
import gg.scala.basics.plugin.timeofday.TimeOfDayService
import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.acf.annotation.CommandAlias
import gg.scala.commons.acf.annotation.CommandPermission
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import gg.scala.commons.issuer.ScalaPlayer
import net.evilblock.cubed.util.CC
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author GrowlyX
 * @since 1/9/2023
 */
@AutoRegister
@CommandPermission("scbasics.player-commands")
object TimeOfDayCommands : ScalaCommand()
{
    private val commandCooldown = CacheBuilder.newBuilder()
        .expireAfterWrite(2L, TimeUnit.SECONDS)
        .build<UUID, Long>()!!

    private fun ensureCanPerform(player: ScalaPlayer)
    {
        val cooldown = commandCooldown
            .getIfPresent(player.uniqueId)

        if (cooldown != null)
        {
            throw ConditionFailedException(
                "Please wait ${"%.2f".format(((cooldown + 5000L) - System.currentTimeMillis()) / 1000.0)} more seconds before running this command again."
            )
        }

        commandCooldown.put(
            player.uniqueId, System.currentTimeMillis()
        )
    }

    @CommandAlias("day")
    fun onDay(player: ScalaPlayer)
    {
        ensureCanPerform(player)

        TimeOfDayService.setToDForProfile(player.bukkit(), TimeOfDaySettingValue.Day)
        player.sendMessage("${CC.GREEN}Your time has been set to day!")
    }

    @CommandAlias("sunset")
    fun onSunset(player: ScalaPlayer)
    {
        ensureCanPerform(player)

        TimeOfDayService.setToDForProfile(player.bukkit(), TimeOfDaySettingValue.Sunset)
        player.sendMessage("${CC.GREEN}Your time has been set to sunset!")
    }

    @CommandAlias("night")
    fun onNight(player: ScalaPlayer)
    {
        ensureCanPerform(player)

        TimeOfDayService.setToDForProfile(player.bukkit(), TimeOfDaySettingValue.Night)
        player.sendMessage("${CC.GREEN}Your time has been set to day!")
    }
}
