package world.respect.datalayer.oneroster.rostering

import kotlinx.coroutines.flow.Flow
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.oneroster.rostering.model.OneRosterClass
import world.respect.datalayer.oneroster.rostering.model.OneRosterEnrollment
import world.respect.datalayer.oneroster.rostering.model.OneRosterUser
import world.respect.datalayer.oneroster.rostering.model.composites.ClazzListDetails

interface OneRosterRosterDataSource {

    suspend fun getAllUsers(): List<OneRosterUser>

    suspend fun putUser(user: OneRosterUser)

    suspend fun getAllClasses(): List<OneRosterClass>
    suspend fun getClassBySourcedId(sourcedId: String): OneRosterClass

    suspend fun findClassBySourcedId(
        loadParams: DataLoadParams,
        sourcedId: String
    ): DataLoadState<OneRosterClass>

    suspend fun findClassBySourcedIdAsFlow(guid: String): Flow<DataLoadState<OneRosterClass>>

    suspend fun putClass(clazz: OneRosterClass)

    suspend fun getEnrolmentsByClass(classSourcedId: String)

    suspend fun putEnrollment(enrollment: OneRosterEnrollment)

    suspend fun findAll(
        loadParams: DataLoadParams,
        searchQuery: String? = null,
    ): Flow<DataLoadState<List<ClazzListDetails>>>

}