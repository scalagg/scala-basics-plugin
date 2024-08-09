package gg.scala.basics.plugin.settings.defaults.values.entity

import gg.scala.basics.plugin.commands.profileOf
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import net.evilblock.cubed.entity.hooks.EntityHooks

/**
 * @author GrowlyX
 * @since 2/20/2023
 */
@Service
object EntityVisibilityService
{
    @Configure
    fun configure()
    {
        EntityHooks
            .entityVisibilityDistance {
                val profile = profileOf(it)
                    ?: return@entityVisibilityDistance 48.0

                profile.setting(
                    id = "entity_render_distance",
                    default = EntityVisibilitySettingValue.High
                ).distance
            }
    }
}
