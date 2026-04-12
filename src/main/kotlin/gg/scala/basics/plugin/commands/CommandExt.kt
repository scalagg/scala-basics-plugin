package gg.scala.basics.plugin.commands

import com.cryptomorin.xseries.XMaterial
import gg.scala.basics.plugin.profile.BasicsProfileService
import gg.scala.basics.plugin.settings.defaults.values.StateSettingValue
import gg.scala.commons.acf.ConditionFailedException
import gg.scala.commons.issuer.ScalaPlayer
import gg.scala.commons.scoping.EditScopesMenu
import gg.scala.commons.scoping.ServerScoped
import gg.scala.lemon.player.LemonPlayer
import me.lucko.helper.utils.Players
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun LemonPlayer.bukkit() = Bukkit.getPlayer(this.uniqueId)!!

fun cachedBasicProfile(player: ScalaPlayer) = BasicsProfileService
    .find(player.bukkit())
    ?: throw ConditionFailedException(
        "Your profile did not load in properly."
    )

fun profileOf(player: Player) = BasicsProfileService.find(player)

infix fun ScalaPlayer.performed(message: String)
{
    bukkit().performed(message)
}

infix fun CommandSender.performed(message: String)
{
    Players.all()
        .filter {
            it.hasPermission("scbasics.notifications")
        }
        .forEach {
            val profile = BasicsProfileService.find(it)
                ?: return@forEach

            val settingValue = profile.setting(
                id = "security-notifications",
                default = StateSettingValue.ENABLED
            )

            if (settingValue == StateSettingValue.ENABLED)
            {
                it.sendMessage(
                    "${CC.I_GRAY}[${
                        if (this is Player) this.name else "Console"
                    }: ${CC.YELLOW}$message${CC.I_GRAY}]"
                )
            }
        }
}
