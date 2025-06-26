package world.respect.app.view.apps.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.stringResource
import respect.composeapp.generated.resources.Res
import respect.composeapp.generated.resources.add_link
import respect.composeapp.generated.resources.add_from_link
import world.respect.app.app.EnterLink
import world.respect.app.appstate.getTitle
import world.respect.app.view.apps.detail.AppsDetailScreen
import world.respect.app.viewmodel.apps.detail.AppsDetailUiState
import world.respect.app.viewmodel.apps.detail.AppsDetailViewModel
import world.respect.app.viewmodel.apps.list.AppListUiState
import world.respect.app.viewmodel.apps.list.AppListViewModel
import world.respect.datasource.compatibleapps.model.RespectAppManifest

@Composable
fun AppListScreen(
    viewModel: AppListViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    AppListScreen(
        uiState = uiState,
        onClickAddLink = { viewModel.onClickAddLink() },
        onClickApp = { viewModel.onClickApp(it) }
    )
}

@Composable
fun AppListScreen(
    uiState: AppListUiState,
    onClickAddLink: () -> Unit,
    onClickApp: (RespectAppManifest) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item {
            Text(
                text = stringResource(Res.string.add_link),
                style = MaterialTheme.typography.titleLarge,
            )
        }

        item {
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClickAddLink() },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Link,
                        contentDescription = null,
                    )
                },
                headlineContent = {
                    Text(
                        text = stringResource(Res.string.add_from_link),
                    )
                }
            )
        }

        items(uiState.appList) { app ->
            ListItem(
                headlineContent = {
                    Text(
                        text = app.name.getTitle(),
                    )
                },
                supportingContent = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        //"-" is a placeholder for age range/category
                        Text(
                            text = "-",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            text = "-",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                },
                leadingContent = {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.background)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Android,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onClickApp(app)
                    }
            )
        }
    }
}
