package gg.scala.basics.plugin.profile

import gg.scala.basics.plugin.settings.MutableSettingEntryReference
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
    val settings = mutableMapOf<String, MutableSettingEntryReference>()

    var lastRecipient: UUID? = null

    inline fun <reified T : Enum<T>> setting(id: String) =
        settings[id]?.map<T>()

    inline fun <reified T : Enum<T>> setting(id: String, default: T) =
        settings[id]?.map() ?: default

    fun save() = DataStoreObjectControllerCache
        .findNotNull<BasicsProfile>()
        .save(this, DataStoreStorageType.MONGO)
}
