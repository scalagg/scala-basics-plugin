package gg.scala.basics.plugin.bossbar

import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.commons.scoping.selectScoped
import gg.scala.commons.persist.datasync.DataSyncService
import gg.scala.commons.persist.datasync.DataSyncSource
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import me.lucko.helper.Events
import me.lucko.helper.Schedulers
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * @author GrowlyX
 * @since 10/16/2022
 */
@Service
object BossBarService : DataSyncService<BossBarConfig>()
{
    @Inject
    lateinit var plugin: ScalaBasicsPlugin

    @Inject
    lateinit var audiences: BukkitAudiences

    override fun locatedIn() = DataSyncSource.Mongo

    fun localConfig() = cached().configs().selectScoped()

    @Configure
    fun configure()
    {
        Events
            .subscribe(PlayerJoinEvent::class.java)
            .handler {
                playerBossBarViews[it.player.uniqueId] = PlayerBossBarView(
                    audience = audiences.player(it.player)
                )
            }
            .bindWith(plugin)

        Events
            .subscribe(PlayerQuitEvent::class.java)
            .handler {
                playerBossBarViews.remove(it.player.uniqueId)?.dispose()
            }
            .bindWith(plugin)

        Schedulers.async()
            .runRepeating(Runnable {
                updateForAll()
            }, 0L, 1L)
    }

    private val updateLock = ReentrantLock()
    private val playerBossBarViews = ConcurrentHashMap<UUID, PlayerBossBarView>()

    override fun postReload()
    {
        updateLock.withLock {
            playerBossBarViews.forEach { (_, view) ->
                view.dispose()
            }
            playerBossBarViews.clear()
        }

        updateForAll()
    }

    fun updateForAll()
    {
        updateLock.withLock {
            playerBossBarViews.forEach { (id, view) ->
                Bukkit.getPlayer(id)
                    ?: return@forEach

                updateFor(view)
            }
        }
    }

    fun updateFor(view: PlayerBossBarView)
    {
        val local = localConfig()
            ?: return

        if (!local.static)
        {
            val elapsed = System.currentTimeMillis() - view.lastUpdate
            if (elapsed < (local.interval ?: 1000))
            {
                return
            }
        }

        val progression = (view.iteration + 1) / local.contents.size.toFloat()
        view.update(
            if (!local.static)
            {
                local.contents.getOrNull(view.iteration)
                    ?: local.contents.firstOrNull()
                    ?: "???"
            } else
            {
                local.contents.firstOrNull() ?: "???"
            },
            if (local.isDynamicProgress) progression else local.progress,
            local
        )

        if (view.iteration >= local.contents.size - 1)
        {
            view.iteration = 0
        } else
        {
            view.iteration += 1
        }
    }

    override fun keys() = BossBarKeys
    override fun type() = BossBarConfig::class.java
}
