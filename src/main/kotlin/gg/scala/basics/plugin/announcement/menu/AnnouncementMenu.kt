package gg.scala.basics.plugin.announcement.menu

import gg.scala.basics.plugin.announcement.Announcement
import gg.scala.basics.plugin.announcement.AnnouncementService
import gg.scala.basics.plugin.announcement.keys.AnnouncementKey
import gg.scala.basics.plugin.announcement.keys.LogicCondition
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.buttons.AddButton
import net.evilblock.cubed.menu.buttons.TexturedHeadButton
import net.evilblock.cubed.menu.menus.ConfirmMenu
import net.evilblock.cubed.menu.pagination.PaginatedMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Constants
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.prompt.InputPrompt
import net.kyori.adventure.key.Key
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
class AnnouncementMenu : PaginatedMenu()
{
    init
    {
        placeholdBorders = true
        updateAfterClick = true
    }

    override fun size(buttons: Map<Int, Button>) = 36
    override fun getAllPagesButtonSlots() = (10..16) + (19..25)

    override fun getGlobalButtons(player: Player): Map<Int, Button>
    {
        return mutableMapOf(
            3 to ItemBuilder
                .copyOf(
                    object : AddButton()
                    {}
                        .getButtonItem(player)
                )
                .name("${CC.B_GREEN}Create announcement")
                .addToLore(
                    "${CC.WHITE}Creates a new announcement.",
                    "",
                    "${CC.GREEN}Click to create!"
                )
                .toButton { _, _ ->
                    val model = AnnouncementService.cached()
                    model.announcements += Announcement(
                        mutableListOf("Announcement dummy text.")
                    )

                    AnnouncementService.sync(model)

                    player.sendMessage(
                        "${CC.GREEN}You've created a new announcement."
                    )
                },
            5 to ItemBuilder
                .copyOf(
                    object : TexturedHeadButton(Constants.SUNNY_SKY_ICON)
                    {}.getButtonItem(player)
                )
                .name("${CC.B_GREEN}Edit Announcement Config")
                .addToLore(
                    "${CC.WHITE}Edit the following settings:",
                    "${CC.WHITE} - Padding",
                    "${CC.WHITE}   ${CC.GRAY}(add blank lines before/after announcement lines)",
                    "",
                    "${CC.WHITE} - Iteration speed",
                    "${CC.WHITE}   ${CC.GRAY}(how fast the announcements iterate)",
                    "",
                    "${CC.GREEN}Click to edit!"
                )
                .toButton { _, _ ->
                    AnnouncementConfigMenu().openMenu(player)
                }
        )
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()
        val model = AnnouncementService.cached()

        for ((index, announcement) in model.announcements.withIndex())
        {
            buttons[buttons.size] = ItemBuilder
                .of(Material.PAPER)
                .name("${CC.B_GREEN}#${index}")
                .setLore(announcement.lines.map { "${CC.WHITE}$it" })
                .addToLore(
                    "",
                    "${CC.GRAY}Conditions:",
                )
                .apply {
                    if (announcement.conditions.isNullOrEmpty())
                    {
                        addToLore(
                            "${CC.RED}None"
                        )
                    } else
                    {
                        announcement.conditions!!
                            .forEach {
                                addToLore(
                                    (if (it.contextual)
                                        "${CC.WHITE} - ${CC.GRAY}(ctx)"
                                    else
                                        "${CC.WHITE} - ") + " ${
                                             it.getMenuPrefix()
                                        }${CC.WHITE}${it.namespace}:${it.value}"
                                )
                            }
                    }
                }
                .addToLore(
                    "",
                    "${CC.GREEN}Left-Click to edit lines.",
                    "${CC.D_GREEN}Shift-Left-Click to edit conditions",
                    "${CC.GOLD}Shift-Click to apply condition.",
                    "${CC.RED}Q to clear conditions.",
                    "${CC.D_RED}Right-Click to delete."
                )
                .toButton { _, type ->
                    if (type!! == ClickType.DROP)
                    {
                        announcement.conditions?.clear()
                        AnnouncementService.sync(model)
                        openMenu(player)

                        player.sendMessage("${CC.RED}Cleared conditions.")
                        return@toButton
                    }

                    if (type.isShiftClick)
                    {
                        if (type.isLeftClick)
                        {
                            AnnouncementEditorMenu(announcement).openMenu(player)
                            return@toButton
                        } else
                        {
                            if (announcement.conditions == null)
                            {
                                announcement.conditions = mutableListOf()
                            }

                            player.closeInventory()

                            InputPrompt()
                                .withText("${CC.GREEN}Enter a condition key (namespace:key)")
                                .acceptInput { _, input ->
                                    val parts = input.split(":")

                                    announcement.conditions!! += AnnouncementKey(parts[0], parts[1])
                                    AnnouncementService.sync(model)

                                    player.sendMessage("${CC.GREEN}Saved!")
                                    openMenu(player)
                                }
                                .start(player)
                            return@toButton
                        }
                    }

                    if (type.isRightClick)
                    {
                        ConfirmMenu(
                            title = "Deleting announcement #$index",
                            extraInfo = listOf(
                                "${CC.WHITE}Are you sure?"
                            ),
                            confirm = true
                        ) {
                            if (it)
                            {
                                model.announcements.removeAt(index)
                                AnnouncementService.sync(model)

                                player.sendMessage("${CC.RED}Deleted announcement #$index!")
                            }
                        }.openMenu(player)
                    } else
                    {
                        AnnouncementLinesEditor(announcement).openMenu(player)
                    }
                }
        }

        return buttons
    }

    override fun getPrePaginatedTitle(player: Player) = "Announcements"
}
