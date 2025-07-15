package world.respect.shared.domain.account

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import org.koin.core.component.KoinComponent

class RespectAccountManager(
    val settings: Settings
): KoinComponent {

    private val _storedAccounts = MutableStateFlow<List<RespectAccount>>(emptyList())

    val storedAccounts = _storedAccounts.asStateFlow()

    private val _activeAccountSourcedId = MutableStateFlow<String?>(null)

    private val activeAccount: RespectAccount?
        get() = _storedAccounts.value.firstOrNull {
            it.userSourcedId == _activeAccountSourcedId.value
        }

    private val activeAccountFlow: Flow<RespectAccount?> = _storedAccounts.combine(
        _activeAccountSourcedId
    ) { accountList, activeAccountSourcedId ->
        accountList.firstOrNull { it.userSourcedId == activeAccountSourcedId }
    }

    init {

    }


}