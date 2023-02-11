package gg.scala.basics.plugin.announcement.menu

import gg.scala.basics.plugin.announcement.Announcement
import gg.scala.basics.plugin.announcement.AnnouncementService
import net.evilblock.cubed.menu.menus.TextEditorMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Tasks
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/16/2022
 */
class AnnouncementLinesEditor(
    private val announcement: Announcement
) : TextEditorMenu(announcement.lines)
{
    override fun getPrePaginatedTitle(player: Player) =
        "Editing announcement lines"

    override fun onClose(player: Player)
    {
        Tasks.delayed(1L) {
            AnnouncementMenu().openMenu(player)
        }
    }

    override fun onSave(player: Player, list: List<String>)
    {
        announcement.lines.clear()
        announcement.lines.addAll(list)

        AnnouncementService.sync(
            AnnouncementService.cached()
        )

        player.sendMessage(
            "${CC.GREEN}Saved lines for this announcement!"
        )
    }
}
