package world.respect.app.view.clazz

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.stringResource
import respect.composeapp.generated.resources.Res
import respect.composeapp.generated.resources.clazz

@Composable
fun ClazzScreen(navController: NavHostController, viewModel: Any) {
    Text(text = stringResource(Res.string.clazz))
}