package gg.scala.basics.plugin.profile

import gg.scala.basics.plugin.settings.MutableSettingEntryReference
import gg.scala.store.controller.DataStoreObjectControllerCache
import gg.scala.store.storage.storable.IDataStoreObject
import gg.scala.store.storage.type.DataStoreStorageType
import java.util.*

class BasicsProfile(
    override val identifier: UUID
) : IDataStoreObject
{
    val ignoring = mutableSetOf<UUID>()
    val settings = mutableMapOf<String, MutableSettingEntryReference>()

    var lastRecipient: UUID? = null
    var lastRecipientMessageTimestamp: Long? = null

    var totalPlaytime: Long = 0L
    var lastLoginTimestamp: Long = 0L

    fun playtime(): Long
    {
        if (lastLoginTimestamp > 0L)
        {
            return totalPlaytime + (System.currentTimeMillis() - lastLoginTimestamp)
        }
        return totalPlaytime
    }

    inline fun <reified T : Enum<T>> setting(id: String) =
        settings[id]?.map<T>()

    inline fun <reified T : Enum<T>> setting(id: String, default: T) =
        settings[id]?.map() ?: default

    fun save() = DataStoreObjectControllerCache
        .findNotNull<BasicsProfile>()
        .save(this, DataStoreStorageType.MONGO)
}
