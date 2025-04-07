package gg.scala.basics.plugin.bossbar.menu

import com.cryptomorin.xseries.XMaterial
import gg.scala.basics.plugin.bossbar.BossBarService
import gg.scala.basics.plugin.bossbar.ServerBossBar
import gg.scala.commons.scoping.editScopesButton
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.Menu
import net.evilblock.cubed.menu.menus.ConfirmMenu
import net.evilblock.cubed.menu.menus.TextEditorMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.Tasks
import net.evilblock.cubed.util.bukkit.prompt.InputPrompt
import net.evilblock.cubed.util.bukkit.prompt.NumberPrompt
import net.kyori.adventure.bossbar.BossBar
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 1/18/2025
 */
data class ManageBossBarMenu(private val bossBar: ServerBossBar) : Menu("Managing boss bar...")
{
    init
    {
        placeholder = true
    }

    override fun getButtons(player: Player) = mapOf(
        10 to ItemBuilder
            .of(XMaterial.CLOCK)
            .name(
                "${CC.YELLOW}Contents:"
            )
            .setLore(bossBar.contents.toList().mapIndexed { index, item ->
                "${CC.GRAY}${index + 1}) $item"
            })
            .toButton { _, _ ->
                Button.playNeutral(player)

                object : TextEditorMenu(bossBar.contents.toList())
                {
                    override fun getPrePaginatedTitle(player: Player) = "Editing contents..."

                    override fun onClose(player: Player)
                    {
                    }

                    override fun onSave(player: Player, list: List<String>)
                    {
                        with(BossBarService.cached()) {
                            bossBar.contents = list
                            update(bossBar)

                            BossBarService.sync(this)
                        }
                    }
                }.openMenu(player)
            },
        19 to ItemBuilder
            .of(XMaterial.NAME_TAG)
            .name("${CC.YELLOW}Auto Update: ${if (!bossBar.static) "${CC.GREEN}Yes" else "${CC.RED}No"}")
            .toButton { _, _ ->
                Button.playNeutral(player)

                with(BossBarService.cached()) {
                    bossBar.static = !bossBar.static
                    update(bossBar)

                    BossBarService.sync(this)
                }

                openMenu(player)
            },

        20 to ItemBuilder
            .of(XMaterial.EMERALD)
            .name("${CC.YELLOW}Dynamic Progress: ${if (bossBar.isDynamicProgress) "${CC.GREEN}Yes" else "${CC.RED}No"}")
            .toButton { _, _ ->
                Button.playNeutral(player)

                with(BossBarService.cached()) {
                    bossBar.isDynamicProgress = !bossBar.isDynamicProgress
                    update(bossBar)

                    BossBarService.sync(this)
                }

                openMenu(player)
            },
        21 to ItemBuilder
            .of(XMaterial.PAPER)
            .name("${CC.YELLOW}Fixed Progress: ${bossBar.progress}")
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                NumberPrompt()
                    .withText("Enter a value between 0.0-1.0")
                    .acceptInput {
                        with(BossBarService.cached()) {
                            bossBar.progress = it.toFloat().coerceIn(0.0f, 1.0f)
                            update(bossBar)

                            BossBarService.sync(this)
                        }

                        openMenu(player)
                    }
                    .start(player)
            },
        23 to ItemBuilder
            .of(XMaterial.FIREWORK_ROCKET)
            .name("${CC.YELLOW}Auto Update Interval: ${bossBar.interval ?: 1000}")
            .toButton { _, _ ->
                player.closeInventory()
                Button.playNeutral(player)

                NumberPrompt()
                    .withText("Enter a value in ms")
                    .acceptInput {
                        with(BossBarService.cached()) {
                            bossBar.interval = it.toLong()
                            update(bossBar)

                            BossBarService.sync(this)
                        }

                        openMenu(player)
                    }
                    .start(player)
            },
        11 to player.editScopesButton(bossBar, {
            openMenu(it)
        }, { _, tabList ->
            with(BossBarService.cached()) {
                update(tabList)
                BossBarService.sync(this)
            }
        }),
        22 to ItemBuilder
            .of(XMaterial.GREEN_WOOL)
            .name("${CC.YELLOW}Color: ${bossBar.color}")
            .toButton { _, _ ->
                Button.playNeutral(player)
                player.closeInventory()

                InputPrompt()
                    .withText("Enter a color: ${BossBar.Color.entries}")
                    .acceptInput { player, s ->
                        val bossBarColor = BossBar.Color.valueOf(s)
                        with(BossBarService.cached()) {
                            bossBar.color = bossBarColor
                            update(bossBar)

                            BossBarService.sync(this)
                        }

                        openMenu(player)
                    }
                    .start(player)
            },
        16 to ItemBuilder
            .of(XMaterial.BARRIER)
            .name("${CC.RED}Delete Boss Bar")
            .addToLore(
                "${CC.GRAY}This may break QOL, be",
                "${CC.GRAY}careful!"
            )
            .toButton { _, _ ->
                Button.playNeutral(player)

                ConfirmMenu(
                    title = "Delete Boss Bar",
                    confirm = true
                ) { confirmed ->
                    if (confirmed)
                    {
                        Button.playNeutral(player)
                        with(BossBarService.cached()) {
                            internalConfigs.remove(bossBar.id)
                            BossBarService.sync(this)
                        }

                        player.sendMessage("${CC.RED}You have deleted this boss bar!")
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
                ViewBossBarMenu().openMenu(player)
            }
        }
    }
}
