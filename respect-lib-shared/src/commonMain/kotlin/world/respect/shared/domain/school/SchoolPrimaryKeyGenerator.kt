package world.respect.shared.domain.school

import world.respect.datalayer.school.model.Person
import world.respect.datalayer.oneroster.model.OneRosterClass
import world.respect.lib.primarykeygen.PrimaryKeyGenerator

/**
 * Wrapper class used only for purposes of differentiating it for dependency injection purposes
 */
data class SchoolPrimaryKeyGenerator(
    val primaryKeyGenerator: PrimaryKeyGenerator
) {
    companion object {

        val TABLE_IDS = listOf(
            Person.TABLE_ID,
            OneRosterClass.TABLE_ID
        )
    }
}