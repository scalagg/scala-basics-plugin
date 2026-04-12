package gg.scala.basics.plugin.settings.defaults.values.entity

import gg.scala.basics.plugin.settings.SettingValue
import net.evilblock.cubed.entity.hooks.EntityHooks
import net.evilblock.cubed.util.CC
import org.bukkit.entity.Player

enum class EntityVisibilitySettingValue(val distance: Double) : SettingValue
{
    High(48.0),
    Medium(32.0),
    Low(16.0),
    None(0.0);

    override val displayName = "$name${
        // need to use enum name as literal isn't defined here
        if (name == "None") "" else " ${CC.D_GRAY}(${distance.toInt()} blocks)"
    }"

    override fun display(player: Player) = true
}
