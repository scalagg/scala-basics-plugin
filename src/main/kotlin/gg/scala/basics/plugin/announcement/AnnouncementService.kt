package gg.scala.basics.plugin.announcement

import gg.scala.aware.message.AwareMessage
import gg.scala.aware.thread.AwareThreadContext
import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.basics.plugin.announcement.conditions.AnnouncementConditions
import gg.scala.basics.plugin.profile.BasicsProfileService
import gg.scala.basics.plugin.settings.defaults.values.StateSettingValue
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import me.lucko.helper.Events
import me.lucko.helper.Schedulers
import me.lucko.helper.metadata.Metadata
import me.lucko.helper.metadata.MetadataKey
import me.lucko.helper.metadata.MetadataMap
import me.lucko.helper.scheduler.Task
import me.lucko.helper.utils.Players
import net.evilblock.cubed.serializers.Serializers
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.metadata.FixedMetadataValue

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
@Service
object AnnouncementService : Runnable
{
    @Inject
    lateinit var plugin: ScalaBasicsPlugin

    private var task: Task? = null
    private var cached: AnnouncementConfigModel =
        AnnouncementConfigModel()

    private val cache by lazy {
        plugin.aware.internal().connect()
    }

    fun configModel() = this.cached

    private val announcementKey = MetadataKey
        .createIntegerKey("announcementIndex")

    @Configure
    fun configure()
    {
        plugin.aware
            .listen(
                AnnouncementKeys.updateKey
            ) {
                reload()
            }

        reload()
    }

    override fun run()
    {
        if (cached.announcements.isEmpty())
        {
            return
        }

        for (player in Players.all())
        {
            val profile = BasicsProfileService
                .find(player)
                ?: continue

            val announcements = cached
                .announcements.filter {
                    it.conditions?.all { key ->
                        AnnouncementConditions
                            .condition(key)
                            ?.shouldDisplay(player)
                            ?: true
                    } ?: true
                }

            val metadata = Metadata
                .provideForPlayer(player)

            val currentIndex = metadata
                .getOrDefault(
                    announcementKey, -1
                )

            metadata.put(
                announcementKey, currentIndex + 1
            )

            val announcement = announcements
                .getOrNull(currentIndex + 1)
                ?: kotlin.run {
                    metadata.put(
                        announcementKey, -1
                    )

                    cached.announcements.first()
                }

            var linesBeingSent = announcement
                .lines.toMutableList()

            if (cached.padding)
            {
                linesBeingSent.add(0, "")
                linesBeingSent.add("")
            }

            if (cached.linePrefix.isNotEmpty())
            {
                linesBeingSent = linesBeingSent
                    .map { "${cached.linePrefix}$it" }
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

    private fun reload()
    {
        val model = cache.sync()
            .get(
                AnnouncementKeys.storeKey
            )

        if (model == null)
        {
            cached = AnnouncementConfigModel()
            cache.sync()
                .set(
                    AnnouncementKeys.storeKey,
                    Serializers.gson.toJson(cached)
                )

            pushKvUpdate()
            return
        }

        task?.closeAndReportException()
        task = null

        Players.all()
            .forEach {
                it.removeMetadata("announcement", this.plugin)
            }

        this.cached = Serializers.gson
            .fromJson(
                model, AnnouncementConfigModel::class.java
            )
            ?: AnnouncementConfigModel()

        task = Schedulers
            .async()
            .runRepeating(
                this,
                // we're starting this delayed because we don't want the
                // initial announcement being sent EVERY time the new models are saved.
                cached.iterationSpeed, cached.iterationTimeUnit,
                cached.iterationSpeed, cached.iterationTimeUnit
            )
    }

    fun applyKvUpdate(
        newModel: AnnouncementConfigModel
    )
    {
        cached = newModel
        cache.sync()
            .set(
                AnnouncementKeys.storeKey,
                Serializers.gson.toJson(cached)
            )

        pushKvUpdate()
    }

    private fun pushKvUpdate()
    {
        AwareMessage
            .of(
                AnnouncementKeys.updateKey,
                plugin.aware
            )
            .publish(
                AwareThreadContext.SYNC
            )
    }
}
