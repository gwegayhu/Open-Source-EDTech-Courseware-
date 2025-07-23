package world.respect.datalayer.dclazz

import kotlinx.coroutines.flow.Flow
import world.respect.datalayer.dclazz.model.DAssignment
import world.respect.datalayer.dclazz.model.DClazz

interface DClazzDataSource {

    fun allClazzesAsList(): Flow<List<DClazz>>

    suspend fun putAssignment(assignment: DAssignment)

    fun getAssignmentAsFlow(assignmentId: Long): Flow<DAssignment?>

    fun getAllAssignmentsAsFlow(): Flow<List<DAssignment>>

}