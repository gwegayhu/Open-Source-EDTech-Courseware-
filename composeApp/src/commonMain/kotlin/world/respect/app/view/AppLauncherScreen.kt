package world.respect.app.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.kodein.di.compose.localDI
import world.respect.app.AppLauncherModel
import world.respect.app.AppList
import world.respect.app.viewmodel.AppLauncherScreenViewModel

@Composable
fun AppLauncherScreen(navController:NavHostController) {
    val di = localDI()
    val viewModel = AppLauncherScreenViewModel(di)
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.appLauncherDataList.isEmpty()) {

        LazyVerticalGrid(columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()

                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.appLauncherDataList) { app ->
                AppGridItem(app) {
                    navController.navigate(AppList)
                }
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
                AppGridItem(app) {
                    navController.navigate(AppList)
                }
            }
        }
    }
}

@Composable
fun AppGridItem(app: AppLauncherModel, function: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { function() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(text = app.imageText, fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = app.title,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = app.category, fontSize = 12.sp)
            Text(text = app.ageRange, fontSize = 12.sp)
        }
    }
}
