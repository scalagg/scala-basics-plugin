package gg.scala.basics.plugin.announcement

import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.buttons.AddButton
import net.evilblock.cubed.menu.buttons.TexturedHeadButton
import net.evilblock.cubed.menu.menus.ConfirmMenu
import net.evilblock.cubed.menu.pagination.PaginatedMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Constants
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

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
                    val model = AnnouncementService.configModel()
                    model.announcements += Announcement(
                        listOf("Announcement dummy text.")
                    )

                    AnnouncementService.applyKvUpdate(model)

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
        val model = AnnouncementService.configModel()

        for ((index, announcement) in model.announcements.withIndex())
        {
            buttons[buttons.size] = ItemBuilder
                .of(Material.PAPER)
                .name("${CC.B_GREEN}#${index}")
                .setLore(announcement.lines.map { "${CC.WHITE}$it" })
                .addToLore(
                    "",
                    "${CC.GREEN}Click to edit.",
                    "${CC.RED}Right-Click to delete."
                )
                .toButton { _, type ->
                    if (type!!.isRightClick)
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
                                AnnouncementService.applyKvUpdate(model)

                                player.sendMessage("${CC.RED}Deleted announcement #$index!")
                            }
                        }.openMenu(player)
                    }
                }
        }

        return buttons
    }

    override fun getPrePaginatedTitle(player: Player) = "Announcements"
}
