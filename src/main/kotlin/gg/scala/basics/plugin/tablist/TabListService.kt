package gg.scala.basics.plugin.tablist

import gg.scala.aware.message.AwareMessage
import gg.scala.aware.thread.AwareThreadContext
import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import me.lucko.helper.Events
import net.evilblock.cubed.serializers.Serializers
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import org.bukkit.event.player.PlayerJoinEvent

/**
 * @author GrowlyX
 * @since 10/16/2022
 */
@Service
object TabListService
{
    @Inject
    lateinit var plugin: ScalaBasicsPlugin

    @Inject
    lateinit var audiences: BukkitAudiences

    private var cached: TabListConfigModel =
        TabListConfigModel()

    private val cache by lazy {
        plugin.aware.internal().connect()
    }

    fun configModel() = this.cached

    @Configure
    fun configure()
    {
        plugin.aware
            .listen(
                TabListKeys.updateKey
            ) {
                reload()
            }

        Events
            .subscribe(PlayerJoinEvent::class.java)
            .handler {
                val audience = audiences.player(it.player)
                audience.sendPlayerListHeaderAndFooter(cached.header, cached.footer)
            }
            .bindWith(plugin)

        reload()
    }

    private fun reload()
    {
        val model = cache.sync()
            .get(
                TabListKeys.storeKey
            )

        if (model == null)
        {
            cached = TabListConfigModel()
            cache.sync()
                .set(
                    TabListKeys.storeKey,
                    Serializers.gson.toJson(cached)
                )

            pushKvUpdate()
            return
        }

        this.cached = Serializers.gson
            .fromJson(
                model, TabListConfigModel::class.java
            )
            ?: TabListConfigModel()
    }

    fun applyKvUpdate(
        newModel: TabListConfigModel
    )
    {
        cached = newModel
        cache.sync()
            .set(
                TabListKeys.storeKey,
                Serializers.gson.toJson(cached)
            )

        pushKvUpdate()
    }

    private fun pushKvUpdate()
    {
        AwareMessage
            .of(
                TabListKeys.updateKey,
                plugin.aware
            )
            .publish(
                AwareThreadContext.SYNC
            )
    }
}
