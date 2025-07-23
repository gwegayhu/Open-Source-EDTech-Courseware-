package world.respect.app.view.assignments.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import world.respect.app.components.RespectLocalDateField
import world.respect.app.components.defaultItemPadding
import world.respect.datalayer.dclazz.model.DAssignment
import world.respect.shared.viewmodel.assignments.edit.AssignmentEditUiState
import world.respect.shared.viewmodel.assignments.edit.AssignmentEditViewModel

@Composable
fun AssignmentEditScreen(
    viewModel: AssignmentEditViewModel
) {
    val uiState by viewModel.uiState.collectAsState(Dispatchers.Main.immediate)

    AssignmentEditScreen(
        uiState = uiState,
        onEntityChanged = viewModel::onEntityChanged,
    )
}

@Composable
fun AssignmentEditScreen(
    uiState: AssignmentEditUiState,
    onEntityChanged: (DAssignment) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 0.dp, vertical = 16.dp)
    ) {
        OutlinedTextField(
            value = uiState.assignment.title,
            modifier = Modifier.fillMaxWidth().defaultItemPadding(),
            onValueChange = {
                onEntityChanged(uiState.assignment.copy(title = it))
            },
            label = {
                Text("Title")
            }
        )

        OutlinedTextField(
            value = uiState.assignment.description,
            modifier = Modifier.fillMaxWidth().defaultItemPadding(),
            onValueChange = {
                onEntityChanged(uiState.assignment.copy(description = it))
            },
            label = {
                Text("Description")
            }
        )

        RespectLocalDateField(
            value = uiState.assignment.deadline,
            modifier = Modifier.fillMaxWidth().defaultItemPadding(),
            onValueChange = { newDeadline ->
                if(newDeadline != null)
                    onEntityChanged(uiState.assignment.copy(deadline = newDeadline))
            },
            label = {
                Text("Deadline")
            }
        )
    }
}
