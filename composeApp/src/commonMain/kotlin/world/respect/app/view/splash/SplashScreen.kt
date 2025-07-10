package world.respect.app.view.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import world.respect.app.app.AppIcon
import world.respect.app.viewmodel.splash.SplashViewModel

@Composable
fun SplashScreen(
    viewModel: SplashViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    SplashScreen()
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
      AppIcon()
    }
}
