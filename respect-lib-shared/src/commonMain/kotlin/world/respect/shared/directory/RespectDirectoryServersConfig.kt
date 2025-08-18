package world.respect.shared.directory

import kotlinx.serialization.Serializable
import world.respect.datalayer.respect.model.RespectRealmDirectory

/**
 * @property directories a list of RespectDirectoryServer that implement the RESPECT Directory APIs
 */
@Serializable
data class RespectDirectoryServersConfig(
    val directories: List<RespectRealmDirectory>,
)

