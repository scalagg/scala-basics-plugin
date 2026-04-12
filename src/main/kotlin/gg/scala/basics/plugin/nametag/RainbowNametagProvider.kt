package gg.scala.basics.plugin.nametag

import net.evilblock.cubed.nametag.NametagInfo
import net.evilblock.cubed.nametag.NametagProvider
import net.evilblock.cubed.nametag.NametagProviderRegister
import org.bukkit.entity.Player

@NametagProviderRegister
object RainbowNametagProvider : NametagProvider(
    "rainbow", 1000
)
{
    override fun fetchNametag(toRefresh: Player, refreshFor: Player): NametagInfo?
    {
        if (!RainbowNametagService.rainbowNametagEnabled.contains(toRefresh.uniqueId))
        {
            return null
        }

        return RainbowNametagService.nametagInfo
    }
}
