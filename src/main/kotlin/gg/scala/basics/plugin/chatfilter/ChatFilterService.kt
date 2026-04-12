package gg.scala.basics.plugin.chatfilter

import gg.scala.basics.plugin.ScalaBasicsPlugin
import gg.scala.commons.persist.datasync.DataSyncService
import gg.scala.commons.persist.datasync.DataSyncSource
import gg.scala.flavor.inject.Inject
import gg.scala.flavor.service.Service

@Service
object ChatFilterService : DataSyncService<ChatFilterConfigModel>()
{
    @Inject
    lateinit var plugin: ScalaBasicsPlugin

    override fun keys() = ChatFilterKeys
    override fun type() = ChatFilterConfigModel::class.java

    override fun locatedIn() = DataSyncSource.Mongo
}
