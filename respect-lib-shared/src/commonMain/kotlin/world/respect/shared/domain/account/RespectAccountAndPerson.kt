package world.respect.shared.domain.account

import kotlinx.serialization.Serializable
import world.respect.datalayer.realm.model.Person

@Serializable
data class RespectAccountAndPerson(
    val account: RespectAccount,
    val person: Person,
)
