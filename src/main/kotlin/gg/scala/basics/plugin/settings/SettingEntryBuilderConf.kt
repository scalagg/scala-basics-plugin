package gg.scala.basics.plugin.settings

import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
data class SettingEntryBuilderConf(
    var id: String = "",
    var clazz: Class<SettingValue<*>>? = null,
    var item: ItemBuilder = ItemBuilder(Material.AIR),
    var displayName: String = "",
    var displayPredicate: (Player) -> Boolean = { true },
    val description: MutableList<String> = mutableListOf()
)
