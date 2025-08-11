package world.respect.server
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.ktor.http.Url
import io.ktor.server.config.ApplicationConfig
import kotlinx.coroutines.runBlocking
import kotlinx.io.files.Path
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import world.respect.datalayer.RespectAppDataSource
import world.respect.datalayer.RespectRealmDataSource
import world.respect.datalayer.RespectRealmDataSourceLocal
import world.respect.datalayer.db.RespectAppDataSourceDb
import world.respect.datalayer.db.RespectAppDatabase
import world.respect.datalayer.db.RespectRealmDataSourceDb
import world.respect.datalayer.db.RespectRealmDatabase
import world.respect.datalayer.realmdirectory.RealmDirectoryDataSourceLocal
import world.respect.datalayer.respect.model.RespectRealm
import world.respect.lib.primarykeygen.PrimaryKeyGenerator
import world.respect.libutil.ext.sanitizedForFilename
import world.respect.libxxhash.XXStringHasher
import world.respect.libxxhash.jvmimpl.XXStringHasherCommonJvm
import world.respect.server.domain.realm.add.AddRealmUseCase
import world.respect.server.domain.realm.add.AddServerManagedDirectoryCallback
import world.respect.shared.domain.account.setpassword.SetPasswordUseCase
import world.respect.shared.domain.account.setpassword.SetPasswordUseDbImpl
import world.respect.shared.domain.realm.RespectRealmPath
import java.io.File

const val APP_DB_FILENAME = "respect-app.db"

fun serverKoinModule(
    config: ApplicationConfig,
) = module {

    val dataDir = config.absoluteDataDir()

    single<RespectAppDatabase> {
        val dbFile = File(dataDir, APP_DB_FILENAME)
        Room.databaseBuilder<RespectAppDatabase>(dbFile.absolutePath)
            .setDriver(BundledSQLiteDriver())
            .addCallback(AddServerManagedDirectoryCallback(xxStringHasher = get()))
            .build()
    }

    single<Json> {
        Json {
            ignoreUnknownKeys = true
        }
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

    /*
     * Realm scope: realm id = the full realm url as per RespectRealm.self
     */
    scope<RespectRealm> {
        scoped<RespectRealmPath> {
            val realmDirName = Url(id).sanitizedForFilename()
            val realmDirFile = File(dataDir, realmDirName).also {
                if(!it.exists())
                    it.mkdirs()
            }

            RespectRealmPath(
                path = Path(realmDirFile.absolutePath)
            )
        }

        scoped<RespectRealmDatabase> {
            val realmPath: RespectRealmPath = get()
            val appDb: RespectAppDatabase = get()
            val xxHasher: XXStringHasher = get()

            val realmConfig = runBlocking {
                appDb.getRealmConfigEntityDao().findByUid(xxHasher.hash(id))
            } ?: throw IllegalStateException("Realm config not found")

            val realmPathFile = File(realmPath.path.toString())
            val dbFile = realmPathFile.resolve(realmConfig.dbUrl)

            Room.databaseBuilder<RespectRealmDatabase>(dbFile.absolutePath)
                .setDriver(BundledSQLiteDriver())
                .build()
        }

        scoped<SetPasswordUseCase> {
            SetPasswordUseDbImpl(
                realmDb = get(),
                xxHash = get()
            )
        }

        scoped<RespectRealmDataSourceLocal> {
            RespectRealmDataSourceDb(realmDb = get(), xxStringHasher = get())
        }

        scoped<RespectRealmDataSource> {
            get<RespectRealmDataSourceLocal>()
        }
    }

}