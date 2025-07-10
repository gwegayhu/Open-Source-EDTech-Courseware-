package world.respect.datasource.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import world.respect.datasource.db.compatibleapps.daos.CompatibleAppEntityDao
import world.respect.datasource.db.compatibleapps.entities.CompatibleAppEntity
import world.respect.datasource.db.opds.OpdsTypeConverters
import world.respect.datasource.db.opds.daos.OpdsFeedEntityDao
import world.respect.datasource.db.opds.daos.OpdsFeedMetadataEntityDao
import world.respect.datasource.db.opds.daos.OpdsGroupEntityDao
import world.respect.datasource.db.opds.daos.OpdsPublicationEntityDao
import world.respect.datasource.db.opds.daos.ReadiumLinkEntityDao
import world.respect.datasource.db.opds.entities.OpdsFacetEntity
import world.respect.datasource.db.opds.entities.OpdsFeedEntity
import world.respect.datasource.db.opds.entities.OpdsFeedMetadataEntity
import world.respect.datasource.db.opds.entities.OpdsGroupEntity
import world.respect.datasource.db.opds.entities.OpdsPublicationEntity
import world.respect.datasource.db.opds.entities.ReadiumLinkEntity
import world.respect.datasource.db.opds.entities.ReadiumSubjectEntity
import world.respect.datasource.db.shared.SharedConverters
import world.respect.datasource.db.shared.daos.LangMapEntityDao
import world.respect.datasource.db.shared.entities.LangMapEntity

@Database(
    entities = [
        CompatibleAppEntity::class,
        LangMapEntity::class,
        ReadiumLinkEntity::class,
        OpdsPublicationEntity::class,
        ReadiumSubjectEntity::class,
        OpdsFacetEntity::class,
        OpdsGroupEntity::class,
        OpdsFeedEntity::class,
        OpdsFeedMetadataEntity::class,
    ],
    version = 1,
)
@TypeConverters(SharedConverters::class, OpdsTypeConverters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class RespectDatabase: RoomDatabase() {

    abstract fun getCompatibleAppEntityDao(): CompatibleAppEntityDao

    abstract fun getLangMapEntityDao(): LangMapEntityDao

    abstract fun getOpdsFeedEntityDao(): OpdsFeedEntityDao

    abstract fun getOpdsPublicationEntityDao(): OpdsPublicationEntityDao

    abstract fun getOpdsFeedMetadataEntityDao(): OpdsFeedMetadataEntityDao

    abstract fun getReadiumLinkEntityDao(): ReadiumLinkEntityDao

    abstract fun getOpdsGroupEntityDao(): OpdsGroupEntityDao

    companion object {

        val TABLE_IDS = listOf(
            ReadiumLinkEntity.TABLE_ID,
            OpdsPublicationEntity.TABLE_ID,
            OpdsFacetEntity.TABLE_ID,
            OpdsGroupEntity.TABLE_ID,
            OpdsFeedEntity.TABLE_ID,
        )

    }
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<RespectDatabase> {
    override fun initialize(): RespectDatabase
}
