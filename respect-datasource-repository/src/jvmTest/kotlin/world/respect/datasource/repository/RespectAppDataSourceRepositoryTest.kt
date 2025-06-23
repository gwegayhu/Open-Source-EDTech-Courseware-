package world.respect.datasource.repository

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import world.respect.datasource.sqldelight.RespectDb
import java.util.Properties
import kotlin.test.Test

class RespectAppDataSourceRepositoryTest {

    @Test
    fun runIt() {
        val driver: SqlDriver = JdbcSqliteDriver(
            JdbcSqliteDriver.IN_MEMORY, Properties(), RespectDb.Schema
        )
        RespectDb.Schema.create(driver)
        val respectDb = RespectDb(driver)

        val json = Json { ignoreUnknownKeys = true }

        // http://localhost/opds/respect-ds/manifestlist.json

        val okHttpClient = OkHttpClient.Builder()



    }


}