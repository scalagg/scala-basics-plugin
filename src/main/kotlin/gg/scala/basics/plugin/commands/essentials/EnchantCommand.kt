package gg.scala.basics.plugin.commands.essentials

import gg.scala.commons.acf.annotation.*
import gg.scala.commons.annotations.commands.AutoRegister
import gg.scala.commons.command.ScalaCommand
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemUtils
import net.evilblock.cubed.util.bukkit.enchantment.EnchantmentWrapper
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

/**
 * @author Topu
 * @date 11/30/2024
 */
@AutoRegister
object EnchantCommand : ScalaCommand() {

    @CommandAlias("enchant")
    @Syntax("<enchantment> <level> [-h]")
    @CommandCompletion("@enchantments 1")
    @CommandPermission("scbasics.command.enchant")
    fun enchant(sender: Player, enchantment: Enchantment, level: Int) {
        if (level <= 0) {
            sender.sendMessage("${CC.RED}The level must be greater than 0.")
            return
        }

        val wrapper = EnchantmentWrapper.parse(enchantment)

        if (!isHotBar(enchantment.name)) {
            val item = sender.itemInHand ?: run {
                sender.sendMessage("${CC.RED}You must be holding an item.")
                return
            }

            if (levelExceedsMaximum(sender, wrapper, level)) return

            wrapper.enchant(item, level)
            sender.updateInventory()
            sender.sendMessage(
                "${CC.GOLD}Enchanted your ${CC.WHITE}${ItemUtils.getName(item)}${CC.GOLD} with " +
                        "${CC.WHITE}${wrapper.friendlyName}${CC.GOLD} level ${CC.WHITE}$level${CC.GOLD}."
            )
            return
        }

        var enchantedCount = 0
        for (slot in 0..8) {
            val item = sender.inventory.getItem(slot)
            if (item != null && wrapper.canEnchantItem(item)) {
                wrapper.enchant(item, level)
                enchantedCount++
            }
        }

        if (enchantedCount == 0) {
            sender.sendMessage("${CC.RED}No items in your hotbar can be enchanted with ${wrapper.friendlyName}.")
            return
        }

        if (levelExceedsMaximum(sender, wrapper, level)) return

        sender.sendMessage(
            "${CC.PRI}Enchanted ${CC.WHITE}$enchantedCount${CC.GOLD} items with " +
                    "${CC.WHITE}${wrapper.friendlyName}${CC.GOLD} level ${CC.WHITE}$level${CC.GOLD}."
        )
        sender.updateInventory()
    }

    @JvmStatic
    fun isHotBar(enchantment: String?) = enchantment
        ?.contains("-h", ignoreCase = true)
        ?: false

    private fun levelExceedsMaximum(sender: Player, wrapper: EnchantmentWrapper, level: Int): Boolean {
        if (level > wrapper.maxLevel) {
            if (!sender.hasPermission("scbasics.command.enchant.force")) {
                sender.sendMessage(
                    "${CC.RED}The maximum enchanting level for ${wrapper.friendlyName} is ${wrapper.maxLevel}. " +
                            "You provided $level."
                )
                return true
            }
            sender.sendMessage(
                "${CC.RED}${CC.BOLD}WARNING: ${CC.YELLOW}You added ${wrapper.friendlyName} $level " +
                        "to this item. The default maximum value is ${wrapper.maxLevel}."
            )
        }
        return false
    }
}