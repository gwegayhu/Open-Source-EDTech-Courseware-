package world.respect.app.view.person.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import world.respect.app.components.RespectPersonAvatar
import world.respect.datalayer.ext.dataOrNull
import world.respect.datalayer.realm.model.composites.PersonListDetails
import world.respect.shared.util.ext.fullName
import world.respect.shared.viewmodel.person.list.PersonListUiState
import world.respect.shared.viewmodel.person.list.PersonListViewModel

@Composable
fun PersonListScreen(
    viewModel: PersonListViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    PersonListScreen(
        uiState = uiState,
        onClickItem = viewModel::onClickItem
    )
}

@Composable
fun PersonListScreen(
    uiState: PersonListUiState,
    onClickItem: (PersonListDetails) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(uiState.persons.dataOrNull() ?: emptyList()) { person ->
            ListItem(
                modifier = Modifier.clickable {
                    onClickItem(person)
                },
                leadingContent = {
                    RespectPersonAvatar(person.fullName())
                },
                headlineContent = {
                    Text(person.fullName())
                }
            )
        }
    }
}