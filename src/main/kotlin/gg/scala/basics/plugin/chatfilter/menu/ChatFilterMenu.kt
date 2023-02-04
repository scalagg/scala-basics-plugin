package gg.scala.basics.plugin.chatfilter.menu

import gg.scala.basics.plugin.chatfilter.ChatFilterService
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.pagination.PaginatedMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 2/4/2023
 */
class ChatFilterMenu : PaginatedMenu()
{
    init
    {
        placeholdBorders = true
    }

    override fun size(buttons: Map<Int, Button>) = 36
    override fun getAllPagesButtonSlots() = (10..16) + (19..25)
    override fun getMaxItemsPerPage(player: Player) = getAllPagesButtonSlots().size

    override fun getAllPagesButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()

        ChatFilterService.configModel()
            .chatFilters.forEach {
                buttons[buttons.size] = ItemBuilder
                    .of(Material.BOOK)
                    .name("${CC.PRI}${it.id}")
                    .addToLore(
                        "${CC.GRAY}Regex pattern:",
                        "${CC.WHITE}${it.regex.pattern}",
                        "",
                        "${CC.WHITE}Test phrase: ${CC.WHITE}${it.testPhrase}"
                    )
                    .toButton()
            }

        return buttons
    }

    override fun getPrePaginatedTitle(player: Player) = "Chat Filter"
}
