package gg.scala.basics.plugin.conversation.settings

import gg.scala.basics.plugin.settings.SettingValue
import org.bukkit.Bukkit
import org.bukkit.entity.Player

enum class MessagingSettingValue : SettingValue
{
    ENABLED
    {
        override val displayName = "Enabled"
    },

    FRIENDS_ONLY
    {
        override val displayName = "Friends Only"

        override fun display(player: Player) =
            Bukkit.getPluginManager().isPluginEnabled("Friends")
    },

    DISABLED
    {
        override val displayName = "Disabled"
    };

    override fun display(player: Player) = true
}
