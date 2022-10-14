package gg.scala.basics.plugin.profile

import gg.scala.commons.persist.ProfileOrchestrator
import gg.scala.flavor.service.Service
import java.util.*

/**
 * @author GrowlyX
 * @since 10/14/2022
 */
@Service
object BasicsProfileService : ProfileOrchestrator<BasicsProfile>()
{
    override fun new(uniqueId: UUID) = BasicsProfile(uniqueId)
    override fun type() = BasicsProfile::class
}
