package world.respect.app.view.clazz

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
import world.respect.shared.viewmodel.clazz.ClazzListUiState
import world.respect.shared.viewmodel.clazz.ClazzListViewModel

@Composable
fun ClazzScreen(
    viewModel: ClazzListViewModel
) {
    val uiState by viewModel.uiState.collectAsState(ClazzListUiState())
    ClazzScreen(uiState)
}

@Composable
fun ClazzScreen(
    uiState: ClazzListUiState
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(uiState.clazzes) {
            ListItem(
                headlineContent = {
                    Text(text = it.name)
                },
                leadingContent = {
                    RespectPersonAvatar(name = it.name)
                },
                supportingContent = {
                    Text(it.description)
                }
            )
        }
    }
}
