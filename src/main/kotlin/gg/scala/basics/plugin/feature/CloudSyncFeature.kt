package gg.scala.basics.plugin.feature

import gg.scala.basics.plugin.profile.BasicsProfileService
import gg.scala.basics.plugin.settings.defaults.values.StateSettingValue
import gg.scala.cloudsync.shared.discovery.CloudSyncDiscoveryService
import gg.scala.cloudsync.spigot.services.CloudSyncListenerService
import gg.scala.commons.annotations.plugin.SoftDependency
import gg.scala.flavor.service.Configure
import gg.scala.flavor.service.Service
import gg.scala.flavor.service.ignore.IgnoreAutoScan

@Service
@IgnoreAutoScan
@SoftDependency("cloudsync")
object CloudSyncFeature
{
    @Configure
    fun configure()
    {
        CloudSyncDiscoveryService
            .discoverable.assets
            .apply {
                add("gg.scala.basics.plugin:scala-basics-plugin:scala-basics-plugin")
            }

        CloudSyncListenerService
            .filterPlayerCanReceiveNotifications =
            ctx@{
                val profile = BasicsProfileService
                    .find(it.player)
                    ?: return@ctx true

                profile.setting(
                    id = "cs_notifications",
                    default = StateSettingValue.ENABLED
                ) == StateSettingValue.ENABLED
            }
    }
}
