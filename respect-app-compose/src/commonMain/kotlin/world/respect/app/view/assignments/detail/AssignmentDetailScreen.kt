package world.respect.app.view.assignments.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import world.respect.app.components.defaultItemPadding
import world.respect.shared.util.displayStr
import world.respect.shared.viewmodel.assignments.detail.AssignmentDetailUiState
import world.respect.shared.viewmodel.assignments.detail.AssignmentDetailViewModel

@Composable
fun AssignmentDetailScreen(
    viewModel: AssignmentDetailViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    AssignmentDetailScreen(
        uiState = uiState,
        onClickOpen = viewModel::onClickOpen,
    )
}

@Composable
fun AssignmentDetailScreen(
    uiState: AssignmentDetailUiState,
    onClickOpen: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (uiState.isTeacher) {
            TabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = 0,
            ) {
                Tab(
                    selected = true,
                    onClick = { },
                    text = { Text("Overview") }
                )
                Tab(
                    selected = false,
                    onClick = {},
                    text = { Text("Progress") }
                )
            }
        }

        Text(
            modifier = Modifier.defaultItemPadding(),
            text = uiState.assignment?.description ?: ""
        )

        ListItem(
            leadingContent = {
                Icon(Icons.Default.DateRange, contentDescription = "")
            },
            headlineContent = {
                Text(uiState.assignment?.deadline?.displayStr() ?: "")
            },
            supportingContent = {
                Text("Deadline")
            }
        )

        if(!uiState.isTeacher) {
            Button(
                onClick = onClickOpen,
                modifier = Modifier.fillMaxWidth().defaultItemPadding()
            ) {
                Text("Open")
            }
        }
    }
}