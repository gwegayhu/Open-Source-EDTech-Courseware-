package world.respect.app.screens

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import world.respect.app.viewmodel.AppLauncherScreenViewModel

@Composable
fun AppLauncherScreen(viewModel: AppLauncherScreenViewModel) {
    Text(text = viewModel.title)
}
