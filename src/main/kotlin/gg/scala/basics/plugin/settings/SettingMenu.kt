package gg.scala.basics.plugin.settings

import com.cryptomorin.xseries.XMaterial
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

class SettingMenu(
    private val player: Player
) : PaginatedMenu()
{
    companion object
    {
        @JvmStatic var defaultCategory = "General"
        @JvmStatic val stateSettingClazz =
            "gg.scala.basics.plugin.settings.defaults.values.StateSettingValue"
    }

    private val colors = listOf(
        ChatColor.GOLD to 1,
        ChatColor.YELLOW to 4,
        ChatColor.GREEN to 5,
        ChatColor.DARK_AQUA to 9,
        ChatColor.LIGHT_PURPLE to 2,
        ChatColor.DARK_PURPLE to 10,
        ChatColor.BLUE to 11,
        ChatColor.AQUA to 3,
    )

    private val categories = SettingContainer
        .categories()
        .filter {
            it.display(player)
        }
        .filter {
            it.items.isNotEmpty()
        }

    private var category = categories.toList()
        .indexOf(
            categories
                .firstOrNull {
                    it.displayName == defaultCategory
                }
                ?: categories.first()
        )
        .let {
            if (it == -1) 0 else it
        }

    init
    {
        updateAfterClick = true
    }

    private fun paneMaterial(color: ChatColor): XMaterial
    {
        return when (color)
        {
            ChatColor.GOLD -> XMaterial.ORANGE_STAINED_GLASS_PANE
            ChatColor.YELLOW -> XMaterial.YELLOW_STAINED_GLASS_PANE
            ChatColor.GREEN -> XMaterial.LIME_STAINED_GLASS_PANE
            ChatColor.DARK_AQUA -> XMaterial.CYAN_STAINED_GLASS_PANE
            ChatColor.LIGHT_PURPLE -> XMaterial.MAGENTA_STAINED_GLASS_PANE
            ChatColor.DARK_PURPLE -> XMaterial.PURPLE_STAINED_GLASS_PANE
            ChatColor.BLUE -> XMaterial.BLUE_STAINED_GLASS_PANE
            ChatColor.AQUA -> XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE
            else -> XMaterial.WHITE_STAINED_GLASS_PANE
        }
    }

    override fun getGlobalButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()

        for ((index, category) in categories.withIndex())
        {
            val color = colors[index]

            val pane = ItemBuilder
                .of(paneMaterial(color.first))
                .name(
                    "${color.first}${
                        category.displayName
                    }"
                )
                .setLore(
                    category.description.map { "${CC.WHITE}$it" }
                )
                .apply {
                    if (this@SettingMenu.category == index)
                        glow()
                }
                .addToLore(
                    "",
                    "${CC.GREEN}Click to browse!"
                )
                .toButton { _, _ ->
                    this.category = index
                }

            buttons[index] = pane
        }

        return buttons
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button>
    {
        val complete = mutableMapOf<Int, Button>()

        val profile = BasicsProfileService
            .find(player)
            ?: return complete

        val category = categories.getOrNull(category)
            ?: return complete

        val items = category.items
            .filter { entry ->
                entry.display(player)
            }

        for (button in items)
        {
            val status = profile.settings[button.id]
                ?: continue

            val available = button.valueType
                .enumConstants
                .filter { it.display(player) }

            complete[complete.size] = ItemBuilder
                .copyOf(button.item)
                .name(
                    "${CC.GREEN}${button.displayName}"
                )
                .setLore(button.description.map { "${CC.WHITE}$it" })
                .apply {
                    if (!button.customLogic)
                    {
                        val current = status.value
                        addToLore(
                            "",
                            "${CC.WHITE}Current setting:"
                        )

                        if (status.clazz == stateSettingClazz)
                        {
                            addToLore(
                                "${if (current == "ENABLED") CC.GREEN else CC.RED}${
                                    if (current == "ENABLED") "Enabled" else "Disabled"
                                }"
                            )
                            return@apply
                        }

                        for (constant in available)
                        {
                            val enum = constant as Enum<*>
                            val equipped = current == enum.name

                            addToLore(
                                "${if (equipped) "${CC.GREEN}► " else CC.GRAY}${constant.displayName}"
                            )
                        }
                    }
                }
                .apply {
                    if (button.customLogic)
                    {
                        addToLore("", "${CC.YELLOW}Click to open menu!")
                        return@apply
                    }

                    addToLore(
                        "",
                        "${CC.YELLOW}${
                            if (available.size == 2)
                                "Click to toggle!" else "Click to scroll through!"
                        }"
                    )
                }
                .toButton { _, _ ->
                    if (button.customLogic)
                    {
                        button.customLogicLambda.invoke(player)
                        return@toButton
                    }

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

        return complete
    }

    override fun getPrePaginatedTitle(player: Player) = "Your Settings"
}
