package world.respect.app.view.manageuser.otheroptionsignup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import world.respect.app.components.defaultItemPadding
import world.respect.app.components.uiTextStringResource
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.how_passkey_works
import world.respect.shared.generated.resources.sign_in_faster
import world.respect.shared.generated.resources.sign_in_faster_description
import world.respect.shared.generated.resources.sign_up_with_passkey
import world.respect.shared.generated.resources.sign_up_with_password
import world.respect.shared.viewmodel.manageuser.otheroptionsignup.OtherOptionsSignupUiState
import world.respect.shared.viewmodel.manageuser.otheroptionsignup.OtherOptionsSignupViewModel

@Composable
fun OtherOptionsSignupScreen(viewModel: OtherOptionsSignupViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    OtherOptionsSignupScreenContent(
        uiState = uiState,
        onClickPasswordSignup = viewModel::onClickSignupWithPassword,
        onClickPasskeySignup = viewModel::onClickSignupWithPasskey,
        onClickHowPasskeysWork = viewModel::onClickHowPasskeysWork,
    )
}

@Composable
fun OtherOptionsSignupScreenContent(
    uiState: OtherOptionsSignupUiState,
    onClickPasswordSignup: () -> Unit,
    onClickPasskeySignup: () -> Unit,
    onClickHowPasskeysWork: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .defaultItemPadding()
    ) {
        uiState.passkeyError?.let {
            Text(it)
        }
        OutlinedButton(
            onClick = onClickPasswordSignup,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(Res.string.sign_up_with_password))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.sign_in_faster),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = buildAnnotatedString {
                    append(stringResource(Res.string.sign_in_faster_description))
                    append(" ")
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(stringResource(Res.string.how_passkey_works))
                    }
                },
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                ),
                modifier = Modifier.clickable { onClickHowPasskeysWork() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onClickPasskeySignup,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(Res.string.sign_up_with_passkey))
            }
            uiState.generalError?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(uiTextStringResource(it))
            }
        }
    }
}
