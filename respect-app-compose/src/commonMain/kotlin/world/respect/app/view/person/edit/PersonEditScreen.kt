package world.respect.app.view.person.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.stringResource
import world.respect.app.components.defaultItemPadding
import world.respect.datalayer.ext.dataOrNull
import world.respect.datalayer.school.model.Person
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.first_names
import world.respect.shared.generated.resources.last_name
import world.respect.shared.viewmodel.person.edit.PersonEditUiState
import world.respect.shared.viewmodel.person.edit.PersonEditViewModel

@Composable
fun PersonEditScreen(
    viewModel: PersonEditViewModel,
) {
    val uiState by viewModel.uiState.collectAsState(Dispatchers.Main.immediate)
    PersonEditScreen(
        uiState = uiState,
        onEntityChanged = viewModel::onEntityChanged,
    )
}

@Composable
fun PersonEditScreen(
    uiState: PersonEditUiState,
    onEntityChanged: (Person) -> Unit,
) {
    val person = uiState.person.dataOrNull()
    val fieldsEnabled = uiState.fieldsEnabled

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().defaultItemPadding(top = 16.dp),
            value = person?.givenName ?: "",
            label = { Text(stringResource(Res.string.first_names) + "*") },
            onValueChange = { value ->
                person?.also {
                    onEntityChanged(it.copy(givenName = value))
                }
            },
            singleLine = true,
            enabled = fieldsEnabled,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().defaultItemPadding(),
            value = person?.familyName ?: "",
            label = { Text(stringResource(Res.string.last_name) + "*") },
            onValueChange = { value ->
                person?.also {
                    onEntityChanged(it.copy(familyName = value))
                }
            },
            singleLine = true,
        )
    }

}
