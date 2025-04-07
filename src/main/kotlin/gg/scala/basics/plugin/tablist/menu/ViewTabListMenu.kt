package gg.scala.basics.plugin.tablist.menu

import com.cryptomorin.xseries.XMaterial
import gg.scala.basics.plugin.bossbar.menu.ManageBossBarMenu
import gg.scala.basics.plugin.bossbar.menu.ViewBossBarMenu
import gg.scala.basics.plugin.tablist.ServerTabList
import gg.scala.basics.plugin.tablist.TabListService
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.pagination.PaginatedMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.entity.Player
import java.util.*

/**
 * @author GrowlyX
 * @since 1/18/2025
 */
class ViewTabListMenu : PaginatedMenu()
{
    init
    {
        placeholdBorders = true
    }

    override fun getPrePaginatedTitle(player: Player) = "Managing Tab Lists"
    override fun getAllPagesButtonSlots() = listOf(
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25
    )

    override fun getGlobalButtons(player: Player) =
        mutableMapOf(
            4 to ItemBuilder
                .of(XMaterial.WRITABLE_BOOK)
                .name("${CC.D_AQUA}Create a Tab List")
                .addToLore(
                    "${CC.GRAY}Create a new global tab list.",
                    "",
                    "${CC.YELLOW}Click to create!"
                )
                .toButton { _, _ ->
                    Button.playNeutral(player)
                    player.closeInventory()

                    with(TabListService.cached()) {
                        val id = UUID.randomUUID().toString()
                        internalConfigs[id] = ServerTabList(id)
                        TabListService.sync(this)
                    }

                    Button.playSuccess(player)
                    player.sendMessage("${CC.GOLD}You created a new tab list!")
                    ViewBossBarMenu().openMenu(player)
                }
        )

    override fun getAllPagesButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()

        TabListService.cached().configs().forEach {
            buttons[buttons.size] = ItemBuilder
                .of(XMaterial.BOOK)
                .name("${CC.GREEN}${it.scopes.first()}")
                .addToLore(
                    "",
                    "${CC.D_AQUA}Header:",
                    *it.header.toTypedArray(),
                    "",
                    "${CC.D_AQUA}Footer:",
                    *it.footer.toTypedArray(),
                    "",
                    "${CC.YELLOW}Scopes:",
                    *it.scopes.map { scope -> "${CC.WHITE}- $scope" }.toTypedArray(),
                    "",
                    "${CC.D_AQUA}Settings:",
                    "${CC.GRAY}Auto update: ${if (it.shouldAutoUpdate) "${CC.GREEN}Yes" else "${CC.RED}No"}",
                    "",
                    "${CC.YELLOW}Click to manage!"
                )
                .toButton { _, _ ->
                    ManageTabListMenu(it).openMenu(player)
                }
        }

        return buttons
    }

    override fun size(buttons: Map<Int, Button>) = 36
    override fun getMaxItemsPerPage(player: Player) = 14
}
