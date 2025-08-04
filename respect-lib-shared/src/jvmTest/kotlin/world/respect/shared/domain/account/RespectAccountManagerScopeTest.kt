package world.respect.shared.domain.account

import com.russhwolf.settings.PropertiesSettings
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import java.util.Properties
import kotlin.test.Test

class RespectAccountManagerScopeTest: KoinTest {

    val testScopeKoinModule = module {
        single<Json> {
            Json {
                encodeDefaults = false
                ignoreUnknownKeys = true
            }
        }

        single<Settings> {
            PropertiesSettings(Properties())
        }

        single<RespectAccountManager> {
            RespectAccountManager(
                settings = get(),
                json = get(),
            )
        }

        scope<RespectAccount> {
            scoped<AccountDepComponent> {
                AccountDepComponent(accountId = id)
            }
        }

    }

    @Test
    fun givenAddAccountsNewScopedCreated() {
        try {
            startKoin {
                modules(testScopeKoinModule)
            }

            val accountScope = getKoin().createScope<RespectAccount>("user@server")
            val accountComp: AccountDepComponent = accountScope.get()
            println("Got account component: $accountComp")

            val accountScope2 = getKoin().createScope<RespectAccount>("user2@server")
            val accountComp2: AccountDepComponent = accountScope2.get()
            println("Got account component2 : $accountComp2")
        }finally {
            stopKoin()
        }

    }
}