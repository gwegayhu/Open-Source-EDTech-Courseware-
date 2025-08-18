package world.respect.shared.domain.account

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.scope.Scope
import world.respect.datalayer.RespectRealmDataSource
import world.respect.datalayer.respect.model.RESPECT_REALM_JSON_PATH
import world.respect.datalayer.respect.model.RespectRealm
import world.respect.libutil.ext.resolve
import world.respect.shared.domain.account.gettokenanduser.GetTokenAndUserProfileWithUsernameAndPasswordUseCase
import world.respect.shared.domain.realm.MakeRealmPathDirUseCase

/**
 *
 * @property storedAccounts The Stored Accounts is the list of all the accounts that the user has
 *           signed in. The user can use the account switcher to switch between them.
 * @property activeAccount The Active Account is the stored account that the user has currently
 *           selected (eg the one normally displayed in the top right). The activeAccount is null
 *           if the user has not signed in yet.
 */
class RespectAccountManager(
    private val settings: Settings,
    private val json: Json,
    private val tokenManager: RespectTokenManager,
    private val httpClient: HttpClient,
): KoinComponent {

    private val _storedAccounts = MutableStateFlow<List<RespectAccount>>(
        value = settings.getStringOrNull(SETTINGS_KEY_STORED_ACCOUNTS)?.let {
            json.decodeFromString(it)
        } ?: emptyList()
    )

    val storedAccounts = _storedAccounts.asStateFlow()

    private val _activeAccountSourcedId = MutableStateFlow(
        settings.getStringOrNull(SETTINGS_KEY_ACTIVE_ACCOUNT)
    )

    var activeAccount: RespectAccount?
        get() = _storedAccounts.value.firstOrNull {
            it.userSourcedId == _activeAccountSourcedId.value
        }

        set(value) {
            if(value == null) {
                _activeAccountSourcedId.value = null
                settings.remove(SETTINGS_KEY_ACTIVE_ACCOUNT)
            }else {
                if(!_storedAccounts.value.any { it.userSourcedId == value.userSourcedId }) {
                    val newValue = _storedAccounts.updateAndGet { prev ->
                        listOf(value) + prev
                    }
                    settings[SETTINGS_KEY_STORED_ACCOUNTS] = json.encodeToString(newValue)
                }

                _activeAccountSourcedId.value = value.userSourcedId
                settings.set(SETTINGS_KEY_ACTIVE_ACCOUNT, value.userSourcedId)
            }
        }

    val activeAccountFlow: Flow<RespectAccount?> = _storedAccounts.combine(
        _activeAccountSourcedId
    ) { accountList, activeAccountSourcedId ->
        accountList.firstOrNull { it.userSourcedId == activeAccountSourcedId }
    }

    val activeAccountAndPersonFlow: Flow<RespectAccountAndPerson?> = channelFlow {
        activeAccountFlow.collectLatest { account ->
            val person = if(account != null) {
                val accountScope = getOrCreateAccountScope(account)
                val realmDataSource: RespectRealmDataSource = accountScope.get()
                realmDataSource.personDataSource.findByGuid(account.userSourcedId)
            }else {
                null
            }

            if(account != null && person != null) {
                send(RespectAccountAndPerson(account, person))
            }else {
                send(null)
            }
        }
    }

    /**
     *
     */
    suspend fun login(
        username: String,
        password: String,
        realmUrl: Url,
    ) {
        val realmScope = getKoin().getOrCreateScope<RespectRealm>(realmUrl.toString())
        val authUseCase: GetTokenAndUserProfileWithUsernameAndPasswordUseCase = realmScope.get()
        val authResponse = authUseCase(
            username = username,
            password = password,
        )

        //This could/should move to using the datasource instead of httpclient directly
        val realm: RespectRealm = httpClient.get(
            realmUrl.resolve(RESPECT_REALM_JSON_PATH)
        ).body()

        val respectAccount = RespectAccount(
            userSourcedId = authResponse.person.guid,
            realm = realm,
        )

        tokenManager.storeToken("$username@$realmUrl", authResponse.token)

        val accountScope = getOrCreateAccountScope(respectAccount)

        val realmDataSource: RespectRealmDataSource = accountScope.get()
        realmDataSource.personDataSource.putPerson(authResponse.person)

        //now we can get the datalayer by creating a RespectAccount scope
        val mkDirUseCase: MakeRealmPathDirUseCase? = realmScope.getOrNull()
        mkDirUseCase?.invoke()

        //Put in person data source
        //Set active user
        activeAccount = respectAccount
    }

    /**
     * When the RespectAccount scope is created it MUST be linked to the parent Realm scope.
     */
    fun getOrCreateAccountScope(account: RespectAccount): Scope {
        //need to ensure that the scope ids are never going to conflict ourself...
        val realmScope = getKoin().getOrCreateScope<RespectRealm>(account.realm.self.toString())
        val accountScope = getKoin().getScopeOrNull(account.scopeId)
            ?: getKoin().createScope<RespectAccount>(account.scopeId).also {
                it.linkTo(realmScope)
            }

        return accountScope
    }


    companion object {

        const val SETTINGS_KEY_STORED_ACCOUNTS = "accountmanager-storedaccounts"

        const val SETTINGS_KEY_ACTIVE_ACCOUNT = "accountmanager-activeaccount"
    }
}