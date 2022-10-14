package gg.scala.basics.plugin.settings

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
interface SettingCategory : DisplayPredicate, Displayable
{
    val items: List<SettingEntry<Any?>>
}
