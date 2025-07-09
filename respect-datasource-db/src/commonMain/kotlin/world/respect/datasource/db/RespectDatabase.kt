package world.respect.datasource.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import world.respect.datasource.db.compatibleapps.daos.CompatibleAppEntityDao
import world.respect.datasource.db.compatibleapps.entities.CompatibleAppEntity
import world.respect.datasource.db.opds.OpdsTypeConverters
import world.respect.datasource.db.opds.entities.ReadiumLinkEntity
import world.respect.datasource.db.shared.SharedConverters
import world.respect.datasource.db.shared.daos.LangMapEntityDao
import world.respect.datasource.db.shared.entities.LangMapEntity

@Database(
    entities = [
        CompatibleAppEntity::class,
        LangMapEntity::class,
        ReadiumLinkEntity::class,
    ],
    version = 1,
)
@TypeConverters(SharedConverters::class, OpdsTypeConverters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class RespectDatabase: RoomDatabase() {

    abstract fun getCompatibleAppEntityDao(): CompatibleAppEntityDao

    abstract fun getLangMapEntityDao(): LangMapEntityDao

}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<RespectDatabase> {
    override fun initialize(): RespectDatabase
}
