package world.respect.datalayer.oneroster.rostering

import world.respect.datalayer.oneroster.rostering.model.OneRosterClass
import world.respect.datalayer.oneroster.rostering.model.OneRosterEnrollment
import world.respect.datalayer.oneroster.rostering.model.OneRosterUser
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class FakeRosterDataSource : OneRosterRosterDataSource {
    override suspend fun getAllUsers() = emptyList<OneRosterUser>()

    override suspend fun putUser(user: OneRosterUser) {}

    @OptIn(ExperimentalTime::class)
    override suspend fun getAllClasses():List<OneRosterClass>{
        return listOf(
            OneRosterClass(
                sourcedId = "1",
                title = "Math Class",
                dateLastModified = Instant.DISTANT_PAST
            ),
            OneRosterClass(
                sourcedId = "2",
                title = "Science Class",
                dateLastModified = Instant.DISTANT_PAST
            ),
            OneRosterClass(
                sourcedId = "3",
                title = "History Class",
                dateLastModified = Instant.DISTANT_PAST
            )
        )
    }

    override suspend fun putClass(clazz: OneRosterClass) {}

    override suspend fun getEnrolmentsByClass(classSourcedId: String) {}

    override suspend fun putEnrollment(enrollment: OneRosterEnrollment) {}
}
