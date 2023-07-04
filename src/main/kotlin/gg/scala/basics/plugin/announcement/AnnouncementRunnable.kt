package gg.scala.basics.plugin.announcement

import gg.scala.basics.plugin.announcement.conditions.AnnouncementConditions
import gg.scala.basics.plugin.profile.BasicsProfileService
import gg.scala.basics.plugin.settings.defaults.values.StateSettingValue
import me.lucko.helper.metadata.Metadata
import me.lucko.helper.utils.Players

/**
 * @author GrowlyX
 * @since 2/10/2023
 */
object AnnouncementRunnable : Runnable
{
    override fun run()
    {
        if (AnnouncementService.cached().announcements.isEmpty())
        {
            return
        }

        for (player in Players.all())
        {
            val profile = BasicsProfileService
                .find(player)
                ?: continue

            val announcements = AnnouncementService.cached()
                .announcements.filter {
                    it.conditions?.all { key ->
                        AnnouncementConditions
                            .let { conditions ->
                                if (key.namespace().contains("ctx"))
                                    conditions.contextual(key) else conditions.condition(key)
                            }
                            ?.let { condition ->
                                if (condition.contextualKey())
                                    condition.contextualDisplay(player, key)
                                else
                                    condition.shouldDisplay(player)
                            }
                            ?: true
                    } ?: true
                }

            val metadata = Metadata
                .provideForPlayer(player)

            val currentIndex = metadata
                .getOrDefault(
                    AnnouncementService.announcementKey, -1
                )

            metadata.put(
                AnnouncementService.announcementKey, currentIndex + 1
            )

            val announcement = announcements
                .getOrNull(currentIndex + 1)
                ?: kotlin.run {
                    metadata.put(
                        AnnouncementService.announcementKey, -1
                    )

                    AnnouncementService.cached().announcements.first()
                }

            var linesBeingSent = announcement
                .lines.toMutableList()

            if (AnnouncementService.cached().padding)
            {
                linesBeingSent.add(0, "")
                linesBeingSent.add("")
            }

            if (AnnouncementService.cached().linePrefix.isNotEmpty())
            {
                linesBeingSent = linesBeingSent
                    .map { "${AnnouncementService.cached().linePrefix}$it" }
                    .toMutableList()
            }

            val setting = profile
                .setting(
                    id = "announcements",
                    default = StateSettingValue.ENABLED
                )

            if (setting == StateSettingValue.ENABLED)
            {
                linesBeingSent.forEach(player::sendMessage)
            }
        }
    }
}
