package world.respect.datalayer.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import world.respect.datalayer.db.realm.daos.PersonEntityDao
import world.respect.datalayer.db.realm.entities.PersonEntity
import world.respect.datalayer.db.shared.SharedConverters

/**
 * Contains realm-specific entities and DAOs
 */
@Database(
    entities = [
        PersonEntity::class,
    ],
    version = 1,

)
@TypeConverters(SharedConverters::class)
@ConstructedBy(RespectRealmDatabaseConstructor::class)
abstract class RespectRealmDatabase: RoomDatabase() {

    abstract fun getPersonEntityDao(): PersonEntityDao


}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING",
    "KotlinNoActualForExpect"
)
expect object RespectRealmDatabaseConstructor : RoomDatabaseConstructor<RespectRealmDatabase> {
    override fun initialize(): RespectRealmDatabase
}
