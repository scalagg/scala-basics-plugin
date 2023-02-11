package gg.scala.basics.plugin.announcement

import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.commons.persist.datasync.DataSyncService
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Service
import me.lucko.helper.Schedulers
import me.lucko.helper.metadata.MetadataKey
import me.lucko.helper.scheduler.Task
import me.lucko.helper.utils.Players

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
@Service
object AnnouncementService : DataSyncService<AnnouncementConfigModel>()
{
    @Inject
    lateinit var plugin: ScalaBasicsPlugin

    override fun keys() = AnnouncementKeys
    override fun type() = AnnouncementConfigModel::class.java

    private var task: Task? = null
    internal val announcementKey = MetadataKey
        .createIntegerKey("announcementIndex")

    override fun cleanup()
    {
        task?.closeAndReportException()
        task = null

        Players.all()
            .forEach {
                it.removeMetadata(
                    "announcement", this.plugin
                )
            }
    }

    override fun postReload()
    {
        val cached = cached()

        task = Schedulers
            .async()
            .runRepeating(
                AnnouncementRunnable,
                // we're starting this delayed because we don't want the
                // initial announcement being sent EVERY time the new models are saved.
                cached.iterationSpeed, cached.iterationTimeUnit,
                cached.iterationSpeed, cached.iterationTimeUnit
            )
    }
}
