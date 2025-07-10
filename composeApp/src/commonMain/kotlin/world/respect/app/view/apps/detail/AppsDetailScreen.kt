package world.respect.app.view.apps.detail

import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import respect.composeapp.generated.resources.Res
import respect.composeapp.generated.resources.add_app
import respect.composeapp.generated.resources.lessons
import respect.composeapp.generated.resources.try_it
import world.respect.app.app.RespectAsyncImage
import world.respect.app.appstate.getTitle
import world.respect.app.viewmodel.apps.detail.AppsDetailUiState
import world.respect.app.viewmodel.apps.detail.AppsDetailViewModel
import world.respect.app.viewmodel.apps.detail.AppsDetailViewModel.Companion.BUTTONS_ROW
import world.respect.app.viewmodel.apps.detail.AppsDetailViewModel.Companion.LEARNING_UNIT_LIST
import world.respect.app.viewmodel.apps.detail.AppsDetailViewModel.Companion.LESSON_HEADER
import world.respect.app.viewmodel.apps.detail.AppsDetailViewModel.Companion.SCREENSHOT
import world.respect.datasource.DataReadyState

import world.respect.datasource.opds.model.OpdsPublication
import world.respect.datasource.opds.model.ReadiumLink

@Composable
fun AppsDetailScreen(
    viewModel: AppsDetailViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    AppsDetailScreen(
        uiState = uiState,
        onClickLessonList = { viewModel.onClickLessonList() },
        onClickPublication = { viewModel.onClickPublication(it) },
        onClickNavigation = { viewModel.onClickNavigation(it) }

    )
}

@Composable
fun AppsDetailScreen(
    uiState: AppsDetailUiState,
    onClickLessonList: () -> Unit,
    onClickPublication: (OpdsPublication) -> Unit,
    onClickNavigation: (ReadiumLink) -> Unit

) {

    val appDetail = (uiState.appDetail as? DataReadyState)?.data

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ListItem(
                leadingContent = {
                    RespectAsyncImage(
                        uri = "https://respect.world/respect-ds/case_valid/icon.webp",
                        // uiState.appIcon.toString(),
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(80.dp)
                    )
                },
                headlineContent = {
                    Text(text = appDetail?.name?.getTitle() ?: "")
                },
                supportingContent = {
                    Text(
                        text = appDetail?.description?.getTitle() ?: "",
                        maxLines = 1
                    )
                },
                trailingContent = {
                    IconButton(onClick = { /* Options */ }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null,
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item(key = BUTTONS_ROW) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { /* Try it */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(Res.string.try_it))
                }

                OutlinedButton(
                    onClick = { /* Add app */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text(text = stringResource(Res.string.add_app))
                }
            }
        }

        item(key = SCREENSHOT) {
            val screenshots = appDetail?.screenshots.orEmpty()

            if (screenshots.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(
                        count = screenshots.size,
                        key = { index -> screenshots[index].url.toString() }
                    ) { index ->
                        val screenshot = screenshots[index]
                        RespectAsyncImage(
                            uri = screenshot.url.toString(),
                            contentDescription = screenshot.description.getTitle(),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(200.dp)
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)

                        )
                    }
                }
            }
        }

        // Lessons header
        item(key = LESSON_HEADER) {
            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(Res.string.lessons),
                        fontWeight = FontWeight.Bold
                    )
                },
                trailingContent = {
                    IconButton(onClick = { onClickLessonList() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item(key = LEARNING_UNIT_LIST) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                itemsIndexed(
                    items = uiState.navigation,
                    key = { index, navigation ->
                        navigation.href
                    }
                ) { index, navigation ->
                    NavigationList(
                        navigation,
                        onClickNavigation = { onClickNavigation(navigation) }
                    )
                }

                itemsIndexed(
                    items = uiState.publications,
                    key = { index, publication ->
                        publication.metadata.identifier.toString()
                    }
                ) { index, publication ->
                    val href = publication.links.find { it.rel?.equals("self") == true }?.href
                    PublicationList(
                        publication,
                        onClickPublication = { onClickPublication(publication) }
                    )
                }

                uiState.group.forEach { group ->
                    itemsIndexed(
                        items = uiState.navigation,
                        key = { index, navigation ->
                            navigation.href
                        }
                    ) { index, navigation ->
                        NavigationList(
                            navigation,
                            onClickNavigation = { onClickNavigation(navigation) }
                        )
                    }

                    itemsIndexed(
                        items = uiState.publications,
                        key = { index, publication ->
                            publication.metadata.identifier.toString()
                        }
                    ) { index, publication ->
                        val href = publication.links.find { it.rel?.equals("self") == true }?.href
                        PublicationList(
                            publication,
                            onClickPublication = { onClickPublication(publication) })
                    }

                }
            }
        }
    }
}

@Composable
fun NavigationList(
    navigation: ReadiumLink,
    onClickNavigation: (ReadiumLink) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
            .clickable {
                onClickNavigation(navigation)
            }
    ) {
        val iconUrl = navigation.alternate?.find { it.rel?.contains("icon") == true }?.href
        RespectAsyncImage(
            uri = iconUrl ?: "",
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)

        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = navigation.title.toString(),
            maxLines = 3,
            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
        )
    }
}

@Composable
fun PublicationList(
    publication: OpdsPublication, onClickPublication: (OpdsPublication) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
            .clickable { onClickPublication(publication) }
    ) {
        RespectAsyncImage(
            uri = publication.images?.firstOrNull()?.href.toString(),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)

        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = publication.metadata.title.getTitle(),
            maxLines = 1,
        )
    }
}
