package world.respect.app.view.manageuser.createaccount

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import world.respect.app.components.defaultItemPadding
import world.respect.app.components.defaultScreenPadding
import world.respect.app.components.uiTextStringResource
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.username_label
import world.respect.shared.generated.resources.signup_with_passkey
import world.respect.shared.generated.resources.other_options
import world.respect.shared.viewmodel.manageuser.signup.CreateAccountViewModelUiState
import world.respect.shared.viewmodel.manageuser.signup.CreateAccountViewModel

@Composable
fun CreateAccountScreen(viewModel: CreateAccountViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    CreateAccountScreen(
        uiState = uiState,
        onUsernameChanged = viewModel::onUsernameChanged,
        onClickSignupWithPasskey = viewModel::onClickSignupWithPasskey,
        onClickOtherOptions = viewModel::onOtherOptionsClick
    )
}

@Composable
fun CreateAccountScreen(
    uiState: CreateAccountViewModelUiState,
    onUsernameChanged: (String) -> Unit,
    onClickSignupWithPasskey: () -> Unit,
    onClickOtherOptions: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .defaultScreenPadding()
    ) {
        OutlinedTextField(
            value = uiState.username,
            onValueChange = onUsernameChanged,
            label = { Text(stringResource(Res.string.username_label)) },
            placeholder = { Text(stringResource(Res.string.username_label)) },
            singleLine = true,
            isError = uiState.usernameError != null,
            supportingText = uiState.usernameError?.let {  { Text(uiTextStringResource(it)) }},
            modifier = Modifier.fillMaxWidth().defaultItemPadding()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClickSignupWithPasskey,
            modifier = Modifier.fillMaxWidth().defaultItemPadding()
        ) {
            Text(stringResource(Res.string.signup_with_passkey))
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onClickOtherOptions,
            modifier = Modifier.fillMaxWidth().defaultItemPadding()
        ) {
            Text(stringResource(Res.string.other_options))
        }

        uiState.generalError?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(uiTextStringResource(it))
        }
    }
}
