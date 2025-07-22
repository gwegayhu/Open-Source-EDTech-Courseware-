package world.respect.shared.domain.account

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent

class RespectAccountManager(
    private val settings: Settings,
    private val json: Json,
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

    private val activeAccount: RespectAccount?
        get() = _storedAccounts.value.firstOrNull {
            it.userSourcedId == _activeAccountSourcedId.value
        }

    private val activeAccountFlow: Flow<RespectAccount?> = _storedAccounts.combine(
        _activeAccountSourcedId
    ) { accountList, activeAccountSourcedId ->
        accountList.firstOrNull { it.userSourcedId == activeAccountSourcedId }
    }

    companion object {

        const val SETTINGS_KEY_STORED_ACCOUNTS = "accountmanager-storedaccounts"

        const val SETTINGS_KEY_ACTIVE_ACCOUNT = "accountmanager-activeaccount"
    }
}