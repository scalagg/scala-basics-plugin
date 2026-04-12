package gg.scala.basics.plugin.settings

import com.cryptomorin.xseries.XMaterial
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

data class SettingEntryBuilderConf(
    var id: String = "",
    var clazz: Class<out SettingValue>? = null,
    var default: SettingValue? = null,
    var item: ItemBuilder = ItemBuilder(XMaterial.AIR),
    var displayName: String = "",
    var displayPredicate: (Player) -> Boolean = { true },
    var postChange: (Player) -> Unit = { },
    var customLogic: Boolean = false,
    var customLogicLambda: (Player) -> Unit = { },
    val description: MutableList<String> = mutableListOf()
)
