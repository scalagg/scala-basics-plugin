package gg.scala.basics.plugin.announcement.menu

import gg.scala.basics.plugin.announcement.Announcement
import gg.scala.basics.plugin.announcement.AnnouncementService
import gg.scala.basics.plugin.announcement.keys.LogicCondition
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.pagination.PaginatedMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.Tasks
import net.evilblock.cubed.util.bukkit.prompt.InputPrompt
import org.bukkit.Material
import org.bukkit.entity.Player

class AnnouncementEditorMenu(val announcement: Announcement) : PaginatedMenu()
{
    init
    {
        updateAfterClick = true
    }

    override fun size(buttons: Map<Int, Button>): Int
    {
        return 27
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()

        if (announcement.conditions == null) return buttons

        for (condition in announcement.conditions!!)
        {
            buttons[buttons.size] =
                ItemBuilder.of(Material.WOOL)
                    .data(condition.logicCondition.data)
                    .addToLore(
                        "${CC.GRAY}Condition Type: ${CC.WHITE}${condition.logicCondition.color}${condition.logicCondition.displayName}",
                        "${CC.GRAY}Contextual: ${CC.WHITE}${if (condition.contextual) "${CC.GREEN}Yes" else "${CC.RED}No"}",
                        "${CC.GRAY}Namespace: ${CC.WHITE}${condition.namespace}",
                        "${CC.GRAY}Value: ${CC.WHITE}${condition.value}",
                        "${CC.GRAY}Reference: ${CC.WHITE}${if (condition.referenceKeyString == null) "${CC.RED}None" else "${CC.WHITE}${condition.referenceKeyString}"}",
                        "",
                        "${CC.GREEN}Left-Click to change condition type",
                        "${CC.YELLOW}Right-Click to change reference condition",
                        "${CC.RED}Shift-Right-Click to change contextual status"
                    )
                    .name("${CC.WHITE}Condition #${buttons.size}")
                    .toButton { _, type ->
                        if (type!!.isLeftClick)
                        {
                            InputPrompt()
                                .withText("${CC.GREEN}Type in the condition that you want to make this key!")
                                .acceptInput { _, s ->
                                    when (s)
                                    {
                                        "and" ->
                                        {
                                            condition.logicCondition = LogicCondition.And
                                        }

                                        "not" ->
                                        {
                                            condition.logicCondition = LogicCondition.Not
                                        }

                                        "or" ->
                                        {
                                            condition.logicCondition = LogicCondition.Or
                                        }
                                    }

                                    player.sendMessage("${CC.GREEN}Updated the condition of this announcement key")

                                    AnnouncementService.sync(
                                        AnnouncementService.cached()
                                    )

                                    AnnouncementEditorMenu(announcement).openMenu(player)
                                }.start(player)
                        } else if (type.isRightClick)
                        {
                            if (type.isShiftClick)
                            {
                                condition.contextual = !condition.contextual

                                AnnouncementService.sync(
                                    AnnouncementService.cached()
                                )
                            } else
                            {
                                InputPrompt()
                                    .withText("${CC.GREEN}Enter another condition to used in OR/XOR statements...")
                                    .acceptInput { _, s ->
                                        condition.referenceKeyString = s

                                        player.sendMessage("${CC.GREEN}Added another condition to this announcement key")

                                        AnnouncementService.sync(
                                            AnnouncementService.cached()
                                        )

                                        AnnouncementEditorMenu(announcement).openMenu(player)
                                    }.start(player)
                            }
                        }
                    }
        }

        return buttons
    }

    override fun getPrePaginatedTitle(player: Player): String = "Editing Announcement"

    override fun onClose(player: Player, manualClose: Boolean)
    {
        if (manualClose)
        {
            Tasks.delayed(1L) {
                AnnouncementMenu().openMenu(player)
            }
        }
    }
}
