package gg.scala.basics.plugin.settings

import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
enum class StateSettingValue : SettingValue
{
    ENABLED
    {
        override val displayName = "Enabled"
    },

    DISABLED
    {
        override val displayName = "Disabled"
    };

    override fun display(player: Player) = true
}
