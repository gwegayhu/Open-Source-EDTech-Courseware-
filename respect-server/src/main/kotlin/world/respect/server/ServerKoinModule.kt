package world.respect.server
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.ktor.server.config.ApplicationConfig
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import world.respect.datalayer.RespectAppDataSource
import world.respect.datalayer.db.RespectAppDataSourceDb
import world.respect.datalayer.db.RespectAppDatabase
import world.respect.datalayer.realmdirectory.RealmDirectoryDataSourceLocal
import world.respect.lib.primarykeygen.PrimaryKeyGenerator
import world.respect.libxxhash.XXStringHasher
import world.respect.libxxhash.jvmimpl.XXStringHasherCommonJvm
import world.respect.server.domain.realm.add.AddRealmUseCase
import java.io.File

const val APP_DB_FILENAME = "respect-app.db"

fun serverKoinModule(
    config: ApplicationConfig,
    json: Json,
) = module {

    val dataDir = config.absoluteDataDir()

    single<RespectAppDatabase> {
        val dbFile = File(dataDir, APP_DB_FILENAME)
        Room.databaseBuilder<RespectAppDatabase>(dbFile.absolutePath)
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    single<Json> {
        json
    }

    single<XXStringHasher> {
        XXStringHasherCommonJvm()
    }

    single<PrimaryKeyGenerator> {
        PrimaryKeyGenerator(RespectAppDatabase.TABLE_IDS)
    }

    single<RespectAppDataSource> {
        RespectAppDataSourceDb(
            respectAppDatabase = get(),
            json = get(),
            xxStringHasher = get(),
            primaryKeyGenerator = get(),
        )
    }

    single<AddRealmUseCase> {
        AddRealmUseCase(
            directoryDataSource = get<RespectAppDataSource>().realmDirectoryDataSource as RealmDirectoryDataSourceLocal
        )
    }

}