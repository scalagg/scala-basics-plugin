package gg.scala.basics.plugin.announcement.conditions

import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.basics.plugin.announcement.conditions.serverscope.ServerScopedCondition
import gg.scala.basics.plugin.announcement.keys.AnnouncementKey
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
    val conditions =
        mutableMapOf<String, AnnouncementDisplayCondition>()

    @Inject
    lateinit var plugin: ScalaBasicsPlugin

    fun contextual(key: AnnouncementKey) =
        conditions[key.namespace]

    fun condition(key: AnnouncementKey) = conditions[key.toString()]
        ?: conditions["${key.namespace}:${key.value}"]

    @Configure
    fun configure()
    {
        conditions[ServerScopedCondition.contextualKeyNamespace()] = ServerScopedCondition

        PreConfigureSubTypeProcessor
            .register<AnnouncementDisplayCondition> {
                if (it.contextualKey())
                {
                    conditions[it.contextualKeyNamespace()] = it
                    return@register
                }

                conditions[it.key().toString()] = it
            }
    }
}
