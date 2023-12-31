package gg.scala.basics.plugin.announcement

import gg.scala.basics.plugin.announcement.conditions.AnnouncementConditions
import gg.scala.basics.plugin.announcement.keys.AnnouncementKey
import gg.scala.basics.plugin.announcement.keys.LogicCondition
import gg.scala.basics.plugin.profile.BasicsProfileService
import gg.scala.basics.plugin.settings.defaults.values.StateSettingValue
import me.lucko.helper.metadata.Metadata
import me.lucko.helper.utils.Players
import net.evilblock.cubed.util.bukkit.Tasks
import net.kyori.adventure.key.Key

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
                    it.value.conditions.values.all { key ->
                            AnnouncementConditions
                                .let { conditions ->
                                    if (key.contextual)
                                        conditions.contextual(key) else conditions.condition(key)
                                }
                                ?.let { condition ->
                                    val id = if (condition.contextualKey())
                                        condition.contextualDisplay(
                                            player,
                                            AnnouncementKey(
                                                key.namespace,
                                                key.value
                                            )
                                        )
                                    else
                                        condition.shouldDisplay(player)

                                    if (key.isCondition(LogicCondition.Not)) !id
                                    if (key.isCondition(LogicCondition.Or)) (id || key.checkReference(player)) else id
                                }
                                ?: true
                        }
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

            if (announcements.isEmpty()) return

            val announcement = announcements
                .getOrDefault(currentIndex + 1, null)
                ?: kotlin.run {
                    metadata.put(
                        AnnouncementService.announcementKey, -1
                    )

                    AnnouncementService.cached().announcements.values.first()
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
