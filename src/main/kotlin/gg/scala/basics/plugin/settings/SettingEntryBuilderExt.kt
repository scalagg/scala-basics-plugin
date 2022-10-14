package gg.scala.basics.plugin.settings

import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
inline fun entry(conf: SettingEntryBuilderConf.() -> Unit): SettingEntry
{
    val config = SettingEntryBuilderConf()
    config.conf()

    return object : SettingEntry
    {
        override val id = config.id
        override val valueType = config.clazz!!
        override val item = config.item

        override fun display(player: Player) =
            config.displayPredicate.invoke(player)

        override val displayName = config.displayName
        override val description = config.description
    }
}
