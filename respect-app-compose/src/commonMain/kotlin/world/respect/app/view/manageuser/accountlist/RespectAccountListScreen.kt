package world.respect.app.view.manageuser.accountlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import world.respect.shared.domain.account.RespectAccount
import world.respect.shared.viewmodel.manageuser.accountlist.RespectAccountListUiState
import world.respect.shared.viewmodel.manageuser.accountlist.RespectAccountListViewModel

@Composable
fun RespectAccountListScreen(
    viewModel: RespectAccountListViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    RespectAccountListScreen(
        uiState = uiState,
        onClickAccount = viewModel::onClickAccount,
        onClickAddAccount = viewModel::onClickAddAccount,
    )
}

@Composable
fun RespectAccountListScreen(
    uiState: RespectAccountListUiState,
    onClickAccount: (RespectAccount) -> Unit,
    onClickAddAccount: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        uiState.activeAccount?.also { activeAccount ->
            AccountListItem(
                account = activeAccount,
                onClickAccount = null,
            )
        }

        HorizontalDivider()

        uiState.accounts.forEach { account ->
            AccountListItem(
                account = account.account,
                onClickAccount = onClickAccount,
            )
        }

        HorizontalDivider()

        ListItem(
            modifier = Modifier.clickable {
                onClickAddAccount()
            },
            headlineContent = {
                Text("Add Account")
            },
            leadingContent = {
                Icon(Icons.Default.Add, contentDescription = "")
            }
        )

    }
}