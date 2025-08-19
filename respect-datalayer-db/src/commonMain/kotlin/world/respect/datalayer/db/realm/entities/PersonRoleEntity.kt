package world.respect.datalayer.db.realm.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import world.respect.datalayer.realm.model.PersonRole

@Entity
data class PersonRoleEntity(
    @PrimaryKey(autoGenerate = true)
    val prUid: Int = 0,
    val prPersonGuidHash: Long,
    val prIsPrimaryRole: Boolean,
    val prRoleType: PersonRole.RoleType,
    val prBeginDate: LocalDate? = null,
    val prEndDate: LocalDate? = null,
)