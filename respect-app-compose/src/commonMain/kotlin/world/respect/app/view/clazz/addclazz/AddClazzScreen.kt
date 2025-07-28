package world.respect.app.view.clazz.addclazz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import world.respect.shared.viewmodel.clazz.addclazz.AddClazzViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.class_name_label
import world.respect.shared.viewmodel.clazz.addclazz.AddClazzUiState


@Composable
fun AddClazzScreen(
    viewModel: AddClazzViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    AddClazzScreen(
        uiState = uiState,
        onClassNameChange = viewModel::onClassNameChange,
        )
}

@Composable
fun AddClazzScreen(
    uiState: AddClazzUiState,
    onClassNameChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        OutlinedTextField(
            value = uiState.className,
            onValueChange = onClassNameChange,
            label = {
                Text(
                    text = stringResource(Res.string.class_name_label)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
            modifier = Modifier.fillMaxWidth(),
        )

    }


}