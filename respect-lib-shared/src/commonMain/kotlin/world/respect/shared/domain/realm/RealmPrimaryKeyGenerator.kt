package world.respect.shared.domain.realm

import world.respect.datalayer.realm.model.Person
import world.respect.datalayer.respect.model.Indicator
import world.respect.datalayer.respect.model.RespectReport
import world.respect.lib.primarykeygen.PrimaryKeyGenerator

/**
 * Wrapper class used only for purposes of differentiating it for dependency injection purposes
 */
data class RealmPrimaryKeyGenerator(
    val primaryKeyGenerator: PrimaryKeyGenerator
) {
    companion object {

        val TABLE_IDS = listOf(Person.TABLE_ID, RespectReport.TABLE_ID, Indicator.TABLE_ID)

    }
}