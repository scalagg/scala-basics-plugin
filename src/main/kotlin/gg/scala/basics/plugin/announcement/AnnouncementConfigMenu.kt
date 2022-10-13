package gg.scala.basics.plugin.announcement

import net.evilblock.cubed.menu.Button
import net.evilblock.cubed.menu.Menu
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.Color
import net.evilblock.cubed.util.bukkit.ItemBuilder
import net.evilblock.cubed.util.bukkit.Tasks
import net.evilblock.cubed.util.bukkit.prompt.InputPrompt
import net.evilblock.cubed.util.bukkit.prompt.NumberPrompt
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.concurrent.TimeUnit

/**
 * @author GrowlyX
 * @since 10/12/2022
 */
class AnnouncementConfigMenu : Menu("Announcement Config")
{
    val model = AnnouncementService.configModel()

    override fun getButtons(player: Player): Map<Int, Button>
    {
        val buttons = mutableMapOf<Int, Button>()
        var allowsUpdate = false

        buttons[buttons.size] = ItemBuilder
            .of(Material.BOW)
            .name("${CC.B_GREEN}Padding")
            .addToLore(
                "${CC.WHITE}State:",
                "${CC.GRAY}${model.padding}",
                "",
                "${CC.GREEN}Click to edit."
            )
            .toButton { _, _ ->
                model.padding = !model.padding
                allowsUpdate = true
            }

        buttons[buttons.size] = ItemBuilder
            .of(Material.EMERALD)
            .name("${CC.B_GREEN}Line Prefix")
            .addToLore(
                "${CC.WHITE}State:",
                "${CC.GRAY}${model.linePrefix}",
                "",
                "${CC.GREEN}Click to edit."
            )
            .toButton { _, _ ->
                player.closeInventory()

                InputPrompt()
                    .acceptInput { _, input ->
                        if (input == "cancel")
                        {
                            player.sendMessage("Cancelled.")
                            openMenu(player)
                            return@acceptInput
                        }

                        val oldLinePrefix = model.linePrefix
                        model.linePrefix = Color.translate(input)

                        player.sendMessage("${CC.SEC}Set prefix to")
                        player.sendMessage(model.linePrefix)

                        player.sendMessage("${CC.SEC}from")
                        player.sendMessage(oldLinePrefix)

                        allowsUpdate = true
                        openMenu(player)
                    }
                    .start(player)
            }

        buttons[buttons.size] = ItemBuilder
            .of(Material.APPLE)
            .name("${CC.B_GREEN}Iteration Speed")
            .addToLore(
                "${CC.WHITE}State:",
                "${CC.GRAY}${model.iterationSpeed}",
                "",
                "${CC.GREEN}Click to edit."
            )
            .toButton { _, _ ->
                player.closeInventory()

                NumberPrompt()
                    .acceptInput { input ->
                        val oldLinePrefix = model.iterationSpeed
                        model.iterationSpeed = input.toLong()

                        player.sendMessage("${CC.SEC}Set speed to")
                        player.sendMessage(model.iterationSpeed.toString())

                        player.sendMessage("${CC.SEC}from")
                        player.sendMessage(oldLinePrefix.toString())

                        allowsUpdate = true
                        openMenu(player)
                    }
                    .start(player)
            }

        buttons[buttons.size] = ItemBuilder
            .of(Material.APPLE)
            .name("${CC.B_GREEN}Iteration Speed")
            .addToLore(
                "${CC.WHITE}State:",
                "${CC.GRAY}${model.iterationSpeed}",
                "",
                "${CC.GREEN}Click to edit."
            )
            .toButton { _, _ ->
                player.closeInventory()

                NumberPrompt()
                    .acceptInput { input ->
                        val oldLinePrefix = model.iterationSpeed
                        model.iterationSpeed = input.toLong()

                        player.sendMessage("${CC.SEC}Set speed to")
                        player.sendMessage(model.iterationSpeed.toString())

                        player.sendMessage("${CC.SEC}from")
                        player.sendMessage(oldLinePrefix.toString())

                        allowsUpdate = true
                        openMenu(player)
                    }
                    .start(player)
            }

        buttons[buttons.size] = ItemBuilder
            .of(Material.ANVIL)
            .name("${CC.B_GREEN}Iteration Speed Time Unit")
            .addToLore(
                "${CC.WHITE}State:",
                "${CC.GRAY}${model.iterationTimeUnit.name}",
                "",
                "${CC.GREEN}Click to edit."
            )
            .toButton { _, _ ->
                player.closeInventory()

                val prompt = InputPrompt()
                    prompt.acceptInput { _, input ->
                        if (input == "cancel")
                        {
                            player.sendMessage("Cancelled.")
                            openMenu(player)
                            return@acceptInput
                        }

                        val unit = kotlin
                            .runCatching {
                                TimeUnit.valueOf(input.uppercase())
                            }
                            .getOrNull()
                            ?: return@acceptInput kotlin.run {
                                player.sendMessage("Invalid unit, try again or type cancel.")

                                Tasks.delayed(2L) {
                                    prompt.start(player)
                                }
                            }

                        val oldTimeUnit = model.iterationTimeUnit
                        model.iterationTimeUnit = unit

                        player.sendMessage("${CC.SEC}Set time unit to")
                        player.sendMessage(model.iterationTimeUnit.name)

                        player.sendMessage("${CC.SEC}from")
                        player.sendMessage(oldTimeUnit.name)

                        allowsUpdate = true
                        openMenu(player)
                    }
                    .start(player)
            }

        buttons[8] = ItemBuilder
            .of(Material.WOOL)
            .data(
                if (allowsUpdate) 5 else 8
            )
            .name("${
                if (allowsUpdate) CC.B_GREEN else CC.GRAY
            }Save")
            .addToLore(
                "",
                "${CC.GREEN}Click to save changes."
            )
            .toButton { _, _ ->
                if (!allowsUpdate)
                {
                    player.sendMessage("${CC.RED}No changes were made.")
                    return@toButton
                }

                AnnouncementService.applyKvUpdate(model)
                player.sendMessage("${CC.GREEN}Changes made! You can leave the menu now.")
            }

        return buttons
    }

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
