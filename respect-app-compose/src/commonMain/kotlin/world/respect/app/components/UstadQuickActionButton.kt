package world.respect.app.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import world.respect.shared.resources.UiText


@Composable
fun UstadQuickActionButton(
    iconContent: (@Composable () -> Unit)?,
    labelText: UiText,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource =  remember { MutableInteractionSource() }

    TextButton(
        modifier = modifier.width(112.dp),
        onClick = onClick,
        interactionSource = interactionSource,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FilledTonalIconButton(
                onClick = onClick,
                interactionSource = interactionSource,
            ) {
                iconContent?.invoke()
            }

            Text(
                text = uiTextStringResource(labelText),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.background),
            )
        }
    }
}

@Composable
fun UstadQuickActionButton(
    imageVector: ImageVector? = null,
    labelText: UiText,
    onClick: (() -> Unit) = {  },
){
    UstadQuickActionButton(
        iconContent = {
            if (imageVector != null) {
                Icon(imageVector = imageVector, contentDescription = null)
            }
        },
        labelText = labelText,
        onClick = onClick
    )
}