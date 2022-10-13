package gg.scala.basics.plugin.announcement

import gg.scala.aware.message.AwareMessage
import gg.scala.aware.thread.AwareThreadContext
import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import me.lucko.helper.Schedulers
import me.lucko.helper.scheduler.Task
import me.lucko.helper.utils.Players
import net.evilblock.cubed.serializers.Serializers
import java.util.concurrent.TimeUnit

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

    private var currentIndex = 0

    private val cache by lazy {
        plugin.aware.internal().connect()
    }

    fun configModel() = this.cached

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
        val announcement = cached
            .announcements.getOrNull(currentIndex++)
            ?: kotlin.run {
                currentIndex = 0
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

        for (player in Players.all())
        {
            linesBeingSent.forEach(player::sendMessage)
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

        currentIndex = 0

        this.cached = Serializers.gson
            .fromJson(
                model, AnnouncementConfigModel::class.java
            )
            ?: AnnouncementConfigModel()

        task = Schedulers
            .async()
            .runRepeating(
                this,
                0L, TimeUnit.SECONDS,
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
