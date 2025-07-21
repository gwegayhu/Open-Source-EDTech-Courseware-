@file:Suppress("UnusedImport")

package world.respect

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import world.respect.shared.datasource.RespectAppDataSourceProvider
import world.respect.shared.datasource.SingleDataSourceProvider
import world.respect.shared.viewmodel.apps.detail.AppsDetailViewModel
import world.respect.shared.viewmodel.apps.enterlink.EnterLinkViewModel
import world.respect.shared.viewmodel.apps.launcher.AppLauncherViewModel
import world.respect.shared.viewmodel.apps.list.AppListViewModel
import world.respect.shared.viewmodel.assignments.AssignmentViewModel
import world.respect.shared.viewmodel.learningunit.detail.LearningUnitDetailViewModel
import world.respect.shared.viewmodel.learningunit.list.LearningUnitListViewModel
import world.respect.shared.viewmodel.report.ReportViewModel
import world.respect.datalayer.db.RespectAppDataSourceDb
import world.respect.datalayer.db.RespectDatabase
import world.respect.datalayer.http.RespectAppDataSourceHttp
import world.respect.datalayer.repository.RespectAppDataSourceRepository
import world.respect.lib.primarykeygen.PrimaryKeyGenerator
import world.respect.libxxhash.XXStringHasher
import world.respect.libxxhash.jvmimpl.XXStringHasherCommonJvm
import world.respect.shared.domain.launchapp.LaunchAppUseCase
import world.respect.shared.domain.launchapp.LaunchAppUseCaseAndroid
import world.respect.shared.viewmodel.clazz.list.ClazzListViewModel
import world.respect.shared.viewmodel.clazz.list.detail.ClazzDetailViewModel


@Suppress("unused")
const val DEFAULT_COMPATIBLE_APP_LIST_URL = "https://respect.world/respect-ds/manifestlist.json"

val appKoinModule = module {
    single<Json> {
        Json {
            encodeDefaults = false
            ignoreUnknownKeys = true
        }
    }

    single<XXStringHasher> {
        XXStringHasherCommonJvm()
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .dispatcher(
                Dispatcher().also {
                    it.maxRequests = 30
                    it.maxRequestsPerHost = 10
                }
            ).build()
    }

    single<HttpClient> {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(json = get())
            }
            engine {
                preconfigured = get()
            }
        }
    }

    single<LaunchAppUseCase> {
        LaunchAppUseCaseAndroid(
            appContext = androidContext().applicationContext
        )
    }

    viewModelOf(::AppsDetailViewModel)
    viewModelOf(::AppLauncherViewModel)
    viewModelOf(::EnterLinkViewModel)
    viewModelOf(::AppListViewModel)
    viewModelOf(::AssignmentViewModel)
    viewModelOf(::ClazzListViewModel)
    viewModelOf(::ClazzDetailViewModel)
    viewModelOf(::LearningUnitListViewModel)
    viewModelOf(::LearningUnitDetailViewModel)
    viewModelOf(::ReportViewModel)


    single<RespectAppDataSourceProvider> {
        val appContext = androidContext().applicationContext
        SingleDataSourceProvider(
            datasource = RespectAppDataSourceRepository(
                local = RespectAppDataSourceDb(
                    respectDatabase = Room.databaseBuilder<RespectDatabase>(
                        appContext, appContext.getDatabasePath("respect.db").absolutePath
                    ).setDriver(BundledSQLiteDriver())
                    .build(),
                    json = get(),
                    xxStringHasher = get(),
                    primaryKeyGenerator = PrimaryKeyGenerator(RespectDatabase.TABLE_IDS),
                ),
                remote = RespectAppDataSourceHttp(
                    httpClient = get(),
                    defaultCompatibleAppListUrl = DEFAULT_COMPATIBLE_APP_LIST_URL,
                )
            )
        )
    }
}
