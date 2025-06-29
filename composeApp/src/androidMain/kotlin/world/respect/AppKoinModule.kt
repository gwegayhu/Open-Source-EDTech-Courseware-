package world.respect

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import world.respect.app.fakeds.FakeRespectAppDataSource
import world.respect.app.viewmodel.apps.detail.AppsDetailViewModel
import world.respect.app.viewmodel.apps.enterlink.EnterLinkViewModel
import world.respect.app.viewmodel.apps.launcher.AppLauncherViewModel
import world.respect.app.viewmodel.apps.list.AppListViewModel
import world.respect.app.viewmodel.assignments.AssignmentViewModel
import world.respect.app.viewmodel.clazz.ClazzViewModel
import world.respect.app.viewmodel.lessons.detail.LessonDetailViewModel
import world.respect.app.viewmodel.lessons.list.LessonListViewModel
import world.respect.app.viewmodel.report.ReportViewModel
import world.respect.datasource.RespectAppDataSource
import world.respect.datasource.sqldelight.RespectDb


val appKoinModule = module {
    singleOf(::DummyRepoImpl) {
        bind<DummyRepo>()
    }

    viewModelOf(::AppsDetailViewModel)
    viewModelOf(::AppLauncherViewModel)
    viewModelOf(::EnterLinkViewModel)
    viewModelOf(::AppListViewModel)
    viewModelOf(::AssignmentViewModel)
    viewModelOf(::ClazzViewModel)
    viewModelOf(::LessonListViewModel)
    viewModelOf(::LessonDetailViewModel)
    viewModelOf(::ReportViewModel)

    single<RespectDb> {
        RespectDb(
            driver = AndroidSqliteDriver(RespectDb.Schema, androidContext(), "respect.db")
        )
    }

    single<RespectAppDataSource> {
        FakeRespectAppDataSource()
    }

}
