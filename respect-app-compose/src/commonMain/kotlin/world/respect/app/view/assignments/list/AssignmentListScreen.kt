package world.respect.app.view.assignments.list

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
import world.respect.datalayer.dclazz.model.DAssignment
import world.respect.shared.viewmodel.assignments.list.AssignmentListUiState
import world.respect.shared.viewmodel.assignments.list.AssignmentViewModel

@Composable
fun AssignmentListScreen(
    viewModel: AssignmentViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    AssignmentListScreen(
        uiState = uiState,
        onClickAssignment = viewModel::onClickAssignment,
    )
}

@Composable
fun AssignmentListScreen(
    uiState: AssignmentListUiState,
    onClickAssignment: (DAssignment) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(uiState.assignments) {
            ListItem(
                headlineContent = {
                    Text(it.title)
                },
                leadingContent = {
                    RespectPersonAvatar(name = it.title)
                },
                modifier = Modifier.clickable {
                    onClickAssignment(it)
                }
            )
        }
    }
}
