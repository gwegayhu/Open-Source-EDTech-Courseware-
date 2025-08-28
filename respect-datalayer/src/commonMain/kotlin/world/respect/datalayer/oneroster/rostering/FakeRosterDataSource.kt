package world.respect.datalayer.oneroster.rostering

import world.respect.datalayer.oneroster.rostering.model.OneRosterClass
import world.respect.datalayer.oneroster.rostering.model.OneRosterEnrollment
import world.respect.datalayer.oneroster.rostering.model.OneRosterUser
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
abstract class FakeRosterDataSource : OneRosterDataSource {



    override suspend fun putUser(user: OneRosterUser) {}

    private val classList = mutableListOf<OneRosterClass>()

    init {
        classList.addAll(
            listOf(
                OneRosterClass(
                    sourcedId = "1",
                    title = "Class 1",
                    dateLastModified = Instant.DISTANT_PAST,
                ),
                OneRosterClass(
                    sourcedId = "2",
                    title = "Class 2",
                    dateLastModified = Instant.DISTANT_PAST
                ),
                OneRosterClass(
                    sourcedId = "3",
                    title = "Class 3",
                    dateLastModified = Instant.DISTANT_PAST
                )
            )
        )
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun getAllClasses(): List<OneRosterClass> {
        return classList
    }
    override suspend fun getClassBySourcedId(sourcedId: String): OneRosterClass {
        return classList.first { it.sourcedId == sourcedId }
    }
//    override suspend fun putClass(clazz: OneRosterClass) {
//        val index = classList.indexOfFirst { it.sourcedId == clazz.sourcedId }
//        if (index != -1) {
//            classList[index] = clazz
//        } else {
//            classList.add(clazz)
//        }
//    }


    override suspend fun getEnrolmentsByClass(classSourcedId: String) {}

    override suspend fun putEnrollment(enrollment: OneRosterEnrollment) {}
}
