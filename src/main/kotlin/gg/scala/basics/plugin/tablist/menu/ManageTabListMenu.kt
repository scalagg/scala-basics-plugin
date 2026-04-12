package gg.scala.basics.plugin.tablist.menu

import com.cryptomorin.xseries.XMaterial
import gg.scala.basics.plugin.bossbar.menu.ViewBossBarMenu
import gg.scala.commons.scoping.editScopesButton
import gg.scala.basics.plugin.tablist.ServerTabList
import gg.scala.basics.plugin.tablist.TabListService
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.Menu
import net.evilblock.cubed.menu.menus.ConfirmMenu
import net.evilblock.cubed.menu.menus.TextEditorMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.Tasks
import org.bukkit.entity.Player

data class ManageTabListMenu(private val tabList: ServerTabList) : Menu("Managing tab list...")
{
    init
    {
        placeholder = true
    }

    override fun getButtons(player: Player) = mapOf(
        10 to ItemBuilder
            .of(XMaterial.CLOCK)
            .name(
                "${CC.YELLOW}Header:"
            )
            .setLore(tabList.header)
            .toButton { _, _ ->
                Button.playNeutral(player)

                object : TextEditorMenu(tabList.header.toList())
                {
                    override fun getPrePaginatedTitle(player: Player) = "Editing header..."

                    override fun onClose(player: Player)
                    {
                    }

                    override fun onSave(player: Player, list: List<String>)
                    {
                        with(TabListService.cached()) {
                            tabList.header = list
                            update(tabList)

                            TabListService.sync(this)
                        }
                    }
                }.openMenu(player)
            },
        11 to ItemBuilder
            .of(XMaterial.EMERALD)
            .name(
                "${CC.YELLOW}Footer:"
            )
            .setLore(tabList.footer)
            .toButton { _, _ ->
                Button.playNeutral(player)

                object : TextEditorMenu(tabList.footer.toList())
                {
                    override fun getPrePaginatedTitle(player: Player) = "Editing footer..."

                    override fun onClose(player: Player)
                    {
                    }

                    override fun onSave(player: Player, list: List<String>)
                    {
                        with(TabListService.cached()) {
                            tabList.footer = list
                            update(tabList)

                            TabListService.sync(this)
                        }
                    }
                }.openMenu(player)
            },
        19 to ItemBuilder
            .of(XMaterial.NAME_TAG)
            .name("${CC.YELLOW}Auto Update: ${if (tabList.shouldAutoUpdate) "${CC.GREEN}Yes" else "${CC.RED}No"}")
            .toButton { _, _ ->
                Button.playNeutral(player)

                with(TabListService.cached()) {
                    tabList.shouldAutoUpdate = !tabList.shouldAutoUpdate
                    update(tabList)

                    TabListService.sync(this)
                }

                openMenu(player)
            },
        20 to player.editScopesButton(tabList, {
            openMenu(it)
        }, { _, tabList ->
            with(TabListService.cached()) {
                update(tabList)
                TabListService.sync(this)
            }
        }),
        16 to ItemBuilder
            .of(XMaterial.BARRIER)
            .name("${CC.RED}Delete Tab List")
            .addToLore(
                "${CC.GRAY}This may break QOL, be",
                "${CC.GRAY}careful!"
            )
            .toButton { _, _ ->
                Button.playNeutral(player)

                ConfirmMenu(
                    title = "Delete Tab List",
                    confirm = true
                ) { confirmed ->
                    if (confirmed)
                    {
                        Button.playNeutral(player)
                        with(TabListService.cached()) {
                            internalConfigs.remove(tabList.id)
                            TabListService.sync(this)
                        }

                        player.sendMessage("${CC.RED}You have deleted this tab list!")
                        ViewBossBarMenu().openMenu(player)
                    } else
                    {
                        openMenu(player)
                    }
                }.openMenu(player)
            },
    )

    override fun size(buttons: Map<Int, Button>) = 36
    override fun onClose(player: Player, manualClose: Boolean)
    {
        if (manualClose)
        {
            Tasks.sync {
                ViewTabListMenu().openMenu(player)
            }
        }
    }
}
