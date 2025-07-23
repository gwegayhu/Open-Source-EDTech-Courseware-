package world.respect.app.ds

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.serialization.json.Json
import world.respect.datalayer.dclazz.DClazzDataSource
import world.respect.datalayer.dclazz.model.DAssignment
import world.respect.datalayer.dclazz.model.DClazz

class MockDClazzDataSource(
    private val settings: Settings,
    private val json: Json,
): DClazzDataSource {

    private val assignmentsFlow = MutableStateFlow(
        settings.getStringOrNull(KEY_ASSIGNMENTS)?.let {
            json.decodeFromString<List<DAssignment>>(it)
        } ?: emptyList()
    )


    override fun allClazzesAsList(): Flow<List<DClazz>> {
        return flowOf(
            listOf(
                DClazz("third", "Third Grade", "Third Grade Class")
            )
        )
    }

    override suspend fun putAssignment(assignment: DAssignment) {
        val newList = assignmentsFlow.updateAndGet { prev ->
            prev + assignment
        }

        settings.putString(KEY_ASSIGNMENTS, json.encodeToString(newList))
    }

    override fun getAssignmentAsFlow(assignmentId: Long): Flow<DAssignment?> {
        return assignmentsFlow.map { list ->
            list.firstOrNull { it.assignmentId == assignmentId }
        }
    }

    override fun getAllAssignmentsAsFlow(): Flow<List<DAssignment>> {
        return assignmentsFlow.asStateFlow()
    }

    companion object {

        const val KEY_ASSIGNMENTS = "assignments"
    }

}