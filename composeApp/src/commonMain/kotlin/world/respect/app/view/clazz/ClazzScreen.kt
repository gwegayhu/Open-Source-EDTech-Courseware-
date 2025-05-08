package world.respect.app.view.clazz

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import respect.composeapp.generated.resources.Res
import respect.composeapp.generated.resources.clazz

@Composable
fun ClazzScreen() {
    Text(text = stringResource(Res.string.clazz))
}