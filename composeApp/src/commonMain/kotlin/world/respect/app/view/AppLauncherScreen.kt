package world.respect.app.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kodein.di.compose.localDI
import world.respect.app.AppLauncherModel
import world.respect.app.viewmodel.AppLauncherScreenViewModel

@Composable
fun AppLauncherScreen() {
    val di = localDI()
    val viewModel = AppLauncherScreenViewModel(di)
    val uiState by viewModel.uiState.collectAsState()
    val appUiState by viewModel._appUiState.collectAsState()

    if (uiState.appLauncherDataList.isEmpty()) {

        LazyVerticalGrid(columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.appLauncherDataList) { app ->
                AppGridItem(app)
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.appLauncherDataList) { app ->
                AppGridItem(app)
            }
        }
    }
}

@Composable
fun AppGridItem(app: AppLauncherModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Square image placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(text = app.imageText.take(1), fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Title
        Text(
            text = app.title,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        // Category and Age Range
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = app.category, fontSize = 12.sp)
            Text(text = app.ageRange, fontSize = 12.sp)
        }
    }
}
