package gg.scala.basics.plugin.ignorelist

import gg.scala.cache.uuid.ScalaStoreUuidCache
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.PaginatedResult
import java.util.UUID

object IgnoreListFormatter : PaginatedResult<UUID>()
{
    override fun format(result: UUID, resultIndex: Int): String
    {
        return "${CC.WHITE}- ${ScalaStoreUuidCache.username(result)}"
    }

    override fun getHeader(page: Int, maxPages: Int) =
        "${CC.PRI}=== ${CC.SEC}Ignore List ${CC.GRAY}($page/$maxPages) ${CC.PRI}==="
}
