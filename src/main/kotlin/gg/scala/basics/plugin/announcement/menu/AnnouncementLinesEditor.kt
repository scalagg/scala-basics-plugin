package gg.scala.basics.plugin.announcement.menu

import gg.scala.basics.plugin.announcement.Announcement
import gg.scala.basics.plugin.announcement.AnnouncementService
import net.evilblock.cubed.menu.menus.TextEditorMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Tasks
import org.bukkit.entity.Player

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
        val model = AnnouncementService.cached()

        announcement.lines.clear()
        announcement.lines.addAll(list)
        model.announcements[announcement.id] = announcement

        AnnouncementService.sync(
            model
        )

        player.sendMessage(
            "${CC.GREEN}Saved lines for this announcement!"
        )
    }
}
