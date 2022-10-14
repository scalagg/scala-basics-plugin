package gg.scala.basics.plugin.profile

import gg.scala.basics.plugin.settings.MutableSettingEntryReference
import gg.scala.basics.plugin.settings.SettingContainer
import gg.scala.commons.persist.ProfileOrchestrator
import gg.scala.flavor.service.Service
import java.util.*

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
@Service
object BasicsProfileService : ProfileOrchestrator<BasicsProfile>()
{
    override fun postLoad(uniqueId: UUID)
    {
        val profile = find(uniqueId)
            ?: return

        SettingContainer
            .categories()
            .forEach { category ->
                for (item in category.items)
                {
                    val ref = profile.settings[item.id]

                    if (ref == null)
                    {
                        profile.settings[item.id] =
                            MutableSettingEntryReference(
                                item.valueType.name, (item.default as Enum<*>).name
                            )
                    }
                }
            }
    }

    override fun new(uniqueId: UUID) = BasicsProfile(uniqueId)
    override fun type() = BasicsProfile::class
}
