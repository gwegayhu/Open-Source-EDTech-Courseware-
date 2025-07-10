package world.respect.app.view.manageuser

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import respect.composeapp.generated.resources.*
import world.respect.app.components.uiTextStringResource
import world.respect.app.viewmodel.manageuser.JoinClazzWithCodeUiState
import world.respect.app.viewmodel.manageuser.JoinClazzWithCodeViewModel

@Composable
fun JoinClazzWithCodeScreen(
    viewModel: JoinClazzWithCodeViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    JoinClazzWithCodeScreen(
        uiState = uiState,
        onCodeChanged = viewModel::onCodeChanged,
        onClickNext = viewModel::onClickNext,
        onClickAlreadyHaveAccount = viewModel::onClickAlreadyHaveAccount,
        onClickAddMySchool = viewModel::onClickAddMySchool
    )
}

@Composable
fun JoinClazzWithCodeScreen(
    uiState: JoinClazzWithCodeUiState,
    onCodeChanged: (String) -> Unit,
    onClickNext: () -> Unit,
    onClickAlreadyHaveAccount: () -> Unit,
    onClickAddMySchool: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(Res.string.enter_invite_code_message),
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.inviteCode,
            onValueChange = onCodeChanged,
            label = {
                Text(text = stringResource(Res.string.invite_code_label))
            },
            placeholder = {
                Text(
                    text = stringResource(Res.string.invite_code_label),
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.errorMessage != null,
            supportingText = uiState.errorMessage?.let {
                { Text(uiTextStringResource(it)) }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClickNext,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(Res.string.next),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onClickAlreadyHaveAccount,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(Res.string.already_have_account),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onClickAddMySchool,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(Res.string.add_my_school),
            )
        }
    }
}
