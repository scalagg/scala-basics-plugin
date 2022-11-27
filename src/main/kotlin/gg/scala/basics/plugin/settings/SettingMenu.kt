package gg.scala.basics.plugin.settings

import gg.scala.basics.plugin.profile.BasicsProfileService
import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.Menu
import net.evilblock.cubed.menu.pagination.PaginatedMenu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.Tasks
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
class SettingMenu : Menu("Your Settings")
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

    override fun onClose(player: Player, manualClose: Boolean)
    {
        if (manualClose)
        {
            val profile = BasicsProfileService
                .find(player)
                ?: return

            profile.save()
        }
    }

    override fun getButtons(player: Player): Map<Int, Button>
    {
        val complete = mutableMapOf<Int, Button>()

        val profile = BasicsProfileService
            .find(player)
            ?: return complete

        (0..8).forEach {
            complete[it] = PaginatedMenu.PLACEHOLDER
        }

        SettingContainer.categories()
            .filter {
                it.display(player)
            }
            .onEachIndexed { index, category ->
                if (category.items.isEmpty())
                    return@onEachIndexed

                val items = category.items
                    .filter { entry ->
                        entry.display(player)
                    }

                val color = colors[index]
                var row = (index + 1) * 9

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
                        category.description.map { "${CC.WHITE}$it" }
                    )
                    .toButton()

                complete[row] = pane

                var rawIndex = 0
                var buttonIndex = 0

                while (
                    items.size != rawIndex
                )
                {
                    val position = row + ++buttonIndex

                    if (buttonIndex % 9 == 0)
                    {
                        complete[position] = pane
                        continue
                    }

                    val button = items[rawIndex++]

                    val status = profile.settings[button.id]
                        ?: continue

                    val available = button.valueType
                        .enumConstants
                        .filter { it.display(player) }

                    complete[position] = ItemBuilder
                        .copyOf(button.item)
                        .name(
                            "${CC.GREEN}${button.displayName}"
                        )
                        .setLore(button.description.map { "${CC.WHITE}$it" })
                        .addToLore(
                            "",
                            "${CC.WHITE}Current setting:"
                        )
                        .apply {
                            val current = status.value

                            for (constant in available)
                            {
                                val enum = constant as Enum<*>
                                val equipped = current == enum.name

                                addToLore(
                                    "${if (equipped) "${CC.GREEN}► " else CC.GRAY}${constant.displayName}"
                                )
                            }
                        }
                        .addToLore(
                            "",
                            "${CC.YELLOW}${
                                if (available.size == 2)
                                    "Click to toggle!" else "Click to scroll through!"
                            }"
                        )
                        .toButton { _, _ ->
                            val constants = available
                                .map {
                                    (it as Enum<*>).name
                                }

                            val currentIndex = constants
                                .indexOf(status.value)
                                .let {
                                    if (it == -1) 0 else it
                                }

                            val reachedMaxIndex =
                                currentIndex >= constants.size - 1

                            if (reachedMaxIndex)
                            {
                                status.value = constants.first()
                            } else
                            {
                                status.value = constants
                                    .getOrNull(currentIndex + 1)
                                    ?: constants.first()
                            }

                            button.postChange(player)
                            profile.save()
                        }
                }
            }

        val size = size(complete) + 8
        (size - 8..size).forEach {
            complete[it] = PaginatedMenu.PLACEHOLDER
        }

        return complete
    }
}
