package gg.scala.basics.plugin.settings.defaults

import com.cryptomorin.xseries.XMaterial
import gg.scala.basics.plugin.profile.BasicsProfileService
import gg.scala.basics.plugin.settings.SettingCategory
import gg.scala.basics.plugin.settings.defaults.values.StateSettingValue
import gg.scala.basics.plugin.settings.entry
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import net.evilblock.cubed.serversync.ServerSyncUtilities
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

@Service
object DefaultAdminSettingsCategory : SettingCategory
{
    override val items = listOf(
        entry {
            id = "cs_notifications"
            displayName = "CloudSync Join Messages"

            clazz = StateSettingValue::class.java
            default = StateSettingValue.ENABLED

            description += "Allows you to disable"
            description += "cloudsync plugin update"
            description += "notifications."

            item = ItemBuilder.of(XMaterial.GOLDEN_APPLE)
        },
        entry {
            id = "ss_notifications"
            displayName = "ServerSync Messages"

            clazz = StateSettingValue::class.java
            default = StateSettingValue.ENABLED

            description += "Allows you to disable"
            description += "serversync update"
            description += "notifications."

            item = ItemBuilder.of(XMaterial.GOLDEN_CARROT)
        }
    )

    @Configure
    fun configure()
    {
        ServerSyncUtilities
            .filterPlayerCanReceiveNotifications =
            ctx@{
                val profile = BasicsProfileService
                    .find(it.player)
                    ?: return@ctx true

                profile.setting(
                    id = "ss_notifications",
                    default = StateSettingValue.ENABLED
                ) == StateSettingValue.ENABLED
            }
    }

    override fun display(player: Player) = player.isOp

    override val displayName = "${CC.BD_RED}Admin"
    override val description = listOf(
        "Admin options."
    )
}
