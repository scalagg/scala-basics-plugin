package gg.scala.basics.plugin.tablist

import gg.scala.commons.scoping.ServerScoped
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Constants

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
data class ServerTabList(
    val id: String,
    var header: List<String> = listOf(
        "",
        "${CC.ITALIC}${CC.BOLD}${CC.B_PRI}SCALA",
        "",
    ),
    var footer: List<String> = listOf(
        "",
        "${CC.GRAY}Currently ${CC.WHITE}{playerCount}${CC.GRAY} players globally",
        "${CC.GRAY}     ${CC.WHITE}www${CC.GRAY}.${CC.WHITE}scala${CC.GRAY}.${CC.WHITE}gg ${CC.B_PRI}${Constants.SMALL_DOT_SYMBOL} ${
            "${CC.WHITE}store${CC.GRAY}.${CC.WHITE}scala${CC.GRAY}.${CC.WHITE}gg"
        } ${CC.B_PRI}${Constants.SMALL_DOT_SYMBOL} ${
            "${CC.WHITE}discord${CC.GRAY}.${CC.WHITE}gg${CC.GRAY}/${CC.WHITE}potpvp"
        }     ${CC.GRAY}",
        "",
    ),
    var shouldAutoUpdate: Boolean = true,
    override var scopes: Set<String> = setOf("global")
) : ServerScoped
