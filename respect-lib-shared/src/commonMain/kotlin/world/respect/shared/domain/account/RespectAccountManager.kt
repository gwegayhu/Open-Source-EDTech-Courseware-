package world.respect.shared.domain.account

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import io.ktor.http.Url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import world.respect.datalayer.respect.model.RespectRealm
import world.respect.shared.domain.account.gettokenanduser.GetTokenAndUserProfileWithUsernameAndPasswordUseCase
import world.respect.shared.domain.realm.MakeRealmPathDirUseCase

class RespectAccountManager(
    private val settings: Settings,
    private val json: Json,
    private val tokenManager: RespectTokenManager,
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

    private val activeAccountFlow: Flow<RespectAccount?> = _storedAccounts.combine(
        _activeAccountSourcedId
    ) { accountList, activeAccountSourcedId ->
        accountList.firstOrNull { it.userSourcedId == activeAccountSourcedId }
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

        tokenManager.storeToken("$username@$realmUrl", authResponse.token)

        //now we can get the datalayer by creating a RespectAccount scope
        val mkDirUseCase: MakeRealmPathDirUseCase? = realmScope.getOrNull()
        mkDirUseCase?.invoke()
    }


    companion object {

        const val SETTINGS_KEY_STORED_ACCOUNTS = "accountmanager-storedaccounts"

        const val SETTINGS_KEY_ACTIVE_ACCOUNT = "accountmanager-activeaccount"
    }
}