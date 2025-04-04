package gg.scala.basics.plugin.bossbar.menu

import com.cryptomorin.xseries.XMaterial
import gg.scala.basics.plugin.bossbar.BossBarService
import gg.scala.basics.plugin.bossbar.ServerBossBar
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.pagination.PaginatedMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Constants
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.entity.Player
import java.util.*

/**
 * @author GrowlyX
 * @since 1/18/2025
 */
class ViewBossBarMenu : PaginatedMenu()
{
    init
    {
        placeholdBorders = true
    }

    override fun getPrePaginatedTitle(player: Player) = "Managing Boss Bars"
    override fun getAllPagesButtonSlots() = listOf(
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25
    )

    override fun getGlobalButtons(player: Player) =
        mutableMapOf(
            4 to ItemBuilder
                .of(XMaterial.WRITABLE_BOOK)
                .name("${CC.D_AQUA}Create a Boss Bar")
                .addToLore(
                    "${CC.GRAY}Create a new global boss bar.",
                    "",
                    "${CC.YELLOW}Click to create!"
                )
                .toButton { _, _ ->
                    Button.playNeutral(player)
                    player.closeInventory()

                    with(BossBarService.cached()) {
                        val id = UUID.randomUUID().toString()
                        internalConfigs[id] = ServerBossBar(id)
                        BossBarService.sync(this)
                    }

                    Button.playSuccess(player)
                    player.sendMessage("${CC.GOLD}You created a new boss bar!")
                    ViewBossBarMenu().openMenu(player)
                }
        )

    override fun getAllPagesButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()

        BossBarService.cached().configs().forEach {
            buttons[buttons.size] = ItemBuilder
                .of(XMaterial.BOOK)
                .name("${CC.GREEN}${it.scopes.first()}")
                .addToLore(
                    "",
                    "${CC.D_AQUA}Contents:",
                    *it.contents
                        .mapIndexed { index, s ->
                            "${CC.GRAY}${index + 1}) $s"
                        }
                        .toTypedArray(),
                    "",
                    "${CC.YELLOW}Scopes:",
                    *it.scopes.map { scope -> "${CC.WHITE}- $scope" }.toTypedArray(),
                    "",
                    "${CC.D_AQUA}Settings:",
                    "${CC.GRAY}Color: ${it.color}",
                    "${CC.GRAY}Auto update: ${if (!it.static) "${CC.GREEN}Yes" else "${CC.RED}No"}",
                    "${CC.WHITE}${Constants.THIN_VERTICAL_LINE} ${CC.GRAY}Interval: ${if (it.interval != null) "${it.interval}ms" else "1s"}",
                    "${CC.GRAY}Progress: ",
                    "${CC.WHITE}${Constants.THIN_VERTICAL_LINE} ${CC.GRAY}Dynamic: ${if (it.isDynamicProgress) "${CC.GREEN}Yes" else "${CC.RED}No"}",
                    "${CC.WHITE}${Constants.THIN_VERTICAL_LINE} ${CC.GRAY}Fixed: ${it.progress * 100}%",
                    "",
                    "${CC.YELLOW}Click to manage!"
                )
                .toButton { _, _ ->
                    ManageBossBarMenu(it).openMenu(player)
                }
        }

        return buttons
    }

    override fun size(buttons: Map<Int, Button>) = 36
    override fun getMaxItemsPerPage(player: Player) = 14
}
