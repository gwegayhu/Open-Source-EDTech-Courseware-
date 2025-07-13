package world.respect.app.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource

@Composable
fun uiTextStringResource(uiText: UiText): String {
    return when(uiText) {
        is StringResourceUiText -> {
            stringResource(uiText.resource)
        }
        is StringUiText -> uiText.text
    }
}
