package gg.scala.basics.plugin.settings

import gg.scala.basics.plugin.settings.utils.SettingEnumInternal
import org.bukkit.entity.Player

inline fun entry(conf: SettingEntryBuilderConf.() -> Unit): SettingEntry
{
    val config = SettingEntryBuilderConf()
    config.conf()

    return object : SettingEntry
    {
        override val id = config.id
        override val valueType = config.clazz!!
        override val item = config.item

        override val default = if (config.default == null)
        {
            SettingEnumInternal
                .getFirstConstant(
                    valueType.asSubclass(Enum::class.java)
                ) as SettingValue
        } else
        {
            config.default!!
        }

        override val postChange = config.postChange

        override val customLogic = config.customLogic
        override val customLogicLambda = config.customLogicLambda

        override fun display(player: Player) =
            config.displayPredicate.invoke(player)

        override val displayName = config.displayName
        override val description = config.description
    }
}
