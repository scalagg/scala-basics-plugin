package gg.scala.basics.plugin.settings.defaults.values.entity

import gg.scala.basics.plugin.settings.SettingValue
import net.evilblock.cubed.entity.hooks.EntityHooks
import net.evilblock.cubed.util.CC
import org.bukkit.entity.Player

/**
 * Represents the distance requirement (in the number of Minecraft blocks)
 * a user has to meet before NPC and hologram entities are loaded in.
 *
 * The [EntityHooks] utility is used to override the
 * default <= 32.0D distance requirement with our configurable one.
 *
 * @author GrowlyX
 * @since 2/20/2023
 */
enum class EntityVisibilitySettingValue(val distance: Double) : SettingValue
{
    High(32.0),
    Medium(16.0),
    Low(8.0),
    None(0.0);

    override val displayName = "$name${
        // need to use enum name as literal isn't defined here
        if (name == "None") "" else " ${CC.D_GRAY}(${distance.toInt()} blocks)"
    }"

    override fun display(player: Player) = true
}
