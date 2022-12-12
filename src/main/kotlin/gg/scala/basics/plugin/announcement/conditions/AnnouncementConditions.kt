package gg.scala.basics.plugin.announcement.conditions

import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.commons.preconfigure.PreConfigureSubTypeProcessor
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import net.kyori.adventure.key.Key

/**
 * @author GrowlyX
 * @since 12/12/2022
 */
@Service
object AnnouncementConditions
{
    private val conditions =
        mutableMapOf<String, AnnouncementDisplayCondition>()

    @Inject
    lateinit var plugin: ScalaBasicsPlugin

    fun condition(key: Key) = conditions[key.toString()]

    @Configure
    fun configure()
    {
        PreConfigureSubTypeProcessor
            .register<AnnouncementDisplayCondition> {
                conditions[it.key().toString()] = it
            }
    }
}
