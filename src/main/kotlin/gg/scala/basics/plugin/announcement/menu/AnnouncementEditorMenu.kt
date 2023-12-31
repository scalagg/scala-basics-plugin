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
        placeholder = true
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
                        "${CC.GRAY}Condition Type: &f${condition.logicCondition.color}${condition.logicCondition.displayName}",
                        "${CC.GRAY}Contextual: &f${if (condition.contextual) "${CC.GREEN}Yes" else "${CC.RED}No"}",
                        "${CC.GRAY}Namespace: &f${condition.namespace}",
                        "${CC.GRAY}Value: &f${condition.value}",
                        "",
                        "${CC.GREEN}Left-Click to change condition type"
                    )
                    .name("${CC.GRAY}Condition #${buttons.size}")
                    .toButton { _, _ ->
                        InputPrompt()
                            .withText("${CC.GREEN}Type in the condition that you want to make this key!")
                            .acceptInput { _, s ->
                                when (s)
                                {
                                    "not" -> {
                                        condition.logicCondition = LogicCondition.Not
                                    }

                                    "or" -> {
                                        condition.logicCondition = LogicCondition.Or
                                    }
                                }

                                player.sendMessage("${CC.GREEN}Updated the condition of this announcement key")

                                AnnouncementService.sync(
                                    AnnouncementService.cached()
                                )
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
