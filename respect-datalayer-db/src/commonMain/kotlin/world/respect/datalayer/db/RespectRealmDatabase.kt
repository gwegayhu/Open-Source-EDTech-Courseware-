package world.respect.datalayer.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import world.respect.datalayer.db.realm.RealmTypeConverters
import world.respect.datalayer.db.realm.daos.AuthTokenEntityDao
import world.respect.datalayer.db.realm.daos.IndicatorEntityDao
import world.respect.datalayer.db.realm.daos.PersonEntityDao
import world.respect.datalayer.db.realm.daos.PersonPasswordEntityDao
import world.respect.datalayer.db.realm.daos.PersonRoleEntityDao
import world.respect.datalayer.db.realm.daos.ReportEntityDao
import world.respect.datalayer.db.realm.entities.AuthTokenEntity
import world.respect.datalayer.db.realm.entities.IndicatorEntity
import world.respect.datalayer.db.realm.entities.PersonEntity
import world.respect.datalayer.db.realm.entities.PersonPasswordEntity
import world.respect.datalayer.db.realm.entities.PersonRoleEntity
import world.respect.datalayer.db.realm.entities.ReportEntity
import world.respect.datalayer.db.shared.SharedConverters

/**
 * Contains realm-specific entities and DAOs
 */
@Database(
    entities = [
        PersonEntity::class,
        PersonRoleEntity::class,
        PersonPasswordEntity::class,
        AuthTokenEntity::class,
        ReportEntity::class,
        IndicatorEntity::class
    ],
    version = 1,

)
@TypeConverters(SharedConverters::class, RealmTypeConverters::class)
@ConstructedBy(RespectRealmDatabaseConstructor::class)
abstract class RespectRealmDatabase: RoomDatabase() {

    abstract fun getPersonEntityDao(): PersonEntityDao

    abstract fun getPersonPasswordEntityDao(): PersonPasswordEntityDao

    abstract fun getAuthTokenEntityDao(): AuthTokenEntityDao

    abstract fun getPersonRoleEntityDao(): PersonRoleEntityDao

    abstract fun getReportEntityDao(): ReportEntityDao

    abstract fun getIndicatorEntityDao(): IndicatorEntityDao

}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING",
    "KotlinNoActualForExpect"
)
expect object RespectRealmDatabaseConstructor : RoomDatabaseConstructor<RespectRealmDatabase> {
    override fun initialize(): RespectRealmDatabase
}
