package gg.scala.basics.plugin.timeofday

import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.basics.plugin.profile.BasicsProfileService
import gg.scala.basics.plugin.settings.defaults.values.TimeOfDaySettingValue
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import me.lucko.helper.Events
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent

/**
 * @author GrowlyX
 * @since 1/9/2023
 */
@Service
object TimeOfDayService
{
    @Inject
    lateinit var plugin: ScalaBasicsPlugin

    @Configure
    fun configure()
    {
        Events
            .subscribe(PlayerJoinEvent::class.java)
            .handler {
                toggleToDFromProfile(it.player)
            }
            .bindWith(plugin)
    }

    fun setToDForProfile(
        player: Player, tod: TimeOfDaySettingValue
    )
    {
        val profile = BasicsProfileService
            .find(player)
            ?: return

        profile.settings["time_of_day"]
            ?.apply {
                this.value = tod.name
            }

        toggleBasedOnTimeOfDay(player, tod)
        profile.save()
    }

    fun toggleToDFromProfile(player: Player)
    {
        val profile = BasicsProfileService
            .find(player)
            ?: return

        toggleBasedOnTimeOfDay(
            player,
            profile.setting(
                "time_of_day",
                TimeOfDaySettingValue.Day
            )
        )
    }

    fun toggleBasedOnTimeOfDay(
        player: Player,
        timeOfDay: TimeOfDaySettingValue
    )
    {
        when (timeOfDay)
        {
            TimeOfDaySettingValue.Sunset -> player.setPlayerTime(12500L, false)
            TimeOfDaySettingValue.Night -> player.setPlayerTime(21000L, false)
            else -> player.setPlayerTime(1000L, false)
        }
    }
}
