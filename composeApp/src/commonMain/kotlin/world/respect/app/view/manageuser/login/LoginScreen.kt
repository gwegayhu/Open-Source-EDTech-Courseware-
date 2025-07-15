package world.respect.app.view.manageuser.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import respect.composeapp.generated.resources.*
import world.respect.app.components.defaultItemPadding
import world.respect.app.viewmodel.manageuser.login.LoginUiState
import world.respect.app.viewmodel.manageuser.login.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LoginScreen(
        uiState = uiState,
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onClickLogin = viewModel::onClickLogin
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onClickLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .defaultItemPadding()
    ) {
        OutlinedTextField(
            value = uiState.email,
            onValueChange = onEmailChanged,
            label = { Text(stringResource(Res.string.email_label)) },
            placeholder = { Text(stringResource(Res.string.email_label)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = uiState.emailError != null,
            supportingText = uiState.emailError?.let {
                { Text(it) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = onPasswordChanged,
            label = { Text(stringResource(Res.string.password_label)) },
            placeholder = { Text(stringResource(Res.string.password_label)) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = uiState.passwordError != null,
            supportingText = uiState.passwordError?.let {
                { Text(it) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClickLogin,
            modifier = Modifier
                .fillMaxWidth()
                .defaultItemPadding()
        ) {
            Text(text = stringResource(Res.string.login))
        }
    }
}
