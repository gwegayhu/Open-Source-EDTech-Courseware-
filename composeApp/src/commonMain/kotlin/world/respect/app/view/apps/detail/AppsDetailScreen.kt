package world.respect.app.view.apps.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
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
import respect.composeapp.generated.resources.try_it
import respect.composeapp.generated.resources.add_app
import respect.composeapp.generated.resources.lessons
import world.respect.app.app.RespectAsyncImage
import world.respect.app.appstate.getTitle
import world.respect.app.viewmodel.apps.detail.AppsDetailUiState
import world.respect.app.viewmodel.apps.detail.AppsDetailViewModel
import world.respect.app.viewmodel.apps.detail.AppsDetailViewModel.Companion.BUTTONS_ROW
import world.respect.app.viewmodel.apps.detail.AppsDetailViewModel.Companion.LESSON_HEADER
import world.respect.datasource.DataLoadResult
import world.respect.datasource.opds.model.OpdsPublication

@Composable
fun AppsDetailScreen(
    viewModel: AppsDetailViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    AppsDetailScreen(
        uiState = uiState,
        onClickLessonList = { viewModel.onClickLessonList() },
        onClickLesson = { viewModel.onClickLesson(it) }
    )
}

@Composable
fun AppsDetailScreen(
    uiState: AppsDetailUiState,
    onClickLessonList: () -> Unit,
    onClickLesson: (OpdsPublication) -> Unit
) {

    val appDetail = (uiState.appDetail as? DataLoadResult)?.data

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
                        uri = appDetail?.icon.toString(),
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(80.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                },
                headlineContent = {
                    Text(text = appDetail?.name?.getTitle() ?: "")
                },
                supportingContent = {
                    Text(text = appDetail?.description?.getTitle() ?: "")
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

        item {
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

        // Lessons horizontally scrollable list
        item {
            uiState.publications.let { publications ->
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    itemsIndexed(
                        items = publications,
                        key = { index, publications ->
                            publications.metadata.identifier.toString() ?: index
                        })
                    { index, publication ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(100.dp)
                                .clickable {
                                    onClickLesson(publication)
                                }
                        ) {
                            RespectAsyncImage(
                                uri = publication.images?.firstOrNull()?.href.toString(),
                                contentDescription = "",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = publication.metadata.title.getTitle(),
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
