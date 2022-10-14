package gg.scala.basics.plugin.settings

import gg.scala.basics.plugin.profile.BasicsProfileService
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.Menu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
class SettingMenu : Menu("Preferences")
{
    // yes, this is meant to be a list
    private val colors = listOf(
        ChatColor.GOLD to 1,
        ChatColor.YELLOW to 4,
        ChatColor.GREEN to 5,
        ChatColor.DARK_AQUA to 9,
        ChatColor.LIGHT_PURPLE to 2,
        ChatColor.DARK_PURPLE to 10
    )

    init
    {
        updateAfterClick = true
    }

    override fun getButtons(player: Player): Map<Int, Button>
    {
        val complete = mutableMapOf<Int, Button>()

        val profile = BasicsProfileService
            .find(player)
            ?: return complete

        SettingContainer.categories()
            .filter {
                it.display(player)
            }
            .onEachIndexed { index, category ->
                if (complete.isEmpty())
                    return@onEachIndexed

                val items = category.items
                    .filter { entry ->
                        entry.display(player)
                    }

                val color = colors[index]
                var row = index * 9

                // we need to make sure this is a free slot
                while (
                    complete[row] != null
                )
                {
                    row += 9
                }

                val pane = ItemBuilder
                    .of(Material.STAINED_GLASS_PANE)
                    .data(color.second.toShort())
                    .name(
                        "${color.first}${
                            category.displayName
                        }"
                    )
                    .setLore(
                        category.description
                    )
                    .toButton()

                complete[row] = pane

                var rawIndex = 0
                var buttonIndex = 0

                while (
                    items.size != rawIndex
                )
                {
                    val position =
                        row + ++buttonIndex

                    if (buttonIndex % 9 == 0)
                    {
                        complete[position] = pane
                        continue
                    }

                    val button =
                        items[rawIndex++]

                    complete[position] = ItemBuilder
                        .copyOf(button.item)
                        .name(
                            "${color.first}${CC.BOLD}${button.displayName}"
                        )
                        .setLore(button.description)
                        .apply {
                            val status = profile.settings[button.id]
                                ?: return@apply

                            addToLore("")

                            val current = status.value

                            for (constant in button.valueType.enumConstants)
                            {
                                val enum = constant as Enum<*>
                                val equipped = current == enum.name

                                addToLore(
                                    " ${if (equipped) CC.B_GREEN else CC.B_GRAY}■ ${CC.RESET}${constant.displayName}"
                                )
                            }
                        }
                        .addToLore(
                            "",
                            "${CC.GREEN}Click to toggle!"
                        )
                        .toButton()
                }
            }

        return complete
    }
}
