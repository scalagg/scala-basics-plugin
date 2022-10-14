package gg.scala.basics.plugin.profile

import gg.scala.store.controller.DataStoreObjectControllerCache
import gg.scala.store.storage.storable.IDataStoreObject
import gg.scala.store.storage.type.DataStoreStorageType
import java.util.*

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
class BasicsProfile(
    override val identifier: UUID
) : IDataStoreObject
{
    val ignoring = mutableSetOf<UUID>()

    fun save() = DataStoreObjectControllerCache
        .findNotNull<BasicsProfile>()
        .save(this, DataStoreStorageType.MONGO)
}
