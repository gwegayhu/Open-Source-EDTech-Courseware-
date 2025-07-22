package world.respect.app.view.clazz.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import world.respect.app.app.RespectAsyncImage
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.accept_invite
import world.respect.shared.generated.resources.invite_students
import world.respect.shared.generated.resources.invite_teachers
import world.respect.shared.generated.resources.students
import world.respect.shared.viewmodel.clazz.detail.ClazzDetailUiState
import world.respect.shared.viewmodel.clazz.detail.ClazzDetailViewModel
import world.respect.shared.generated.resources.teachers
@Composable
fun ClazzDetailScreen(
    viewModel: ClazzDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    ClazzDetailScreen(uiState)
}

@Composable
fun ClazzDetailScreen(
    uiState: ClazzDetailUiState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(text =
                stringResource(resource = Res.string.teachers)
            )
        }

        item {
            ListItem(
                leadingContent = {
                    Icon(imageVector = Icons.Filled.Share, contentDescription = null)
                },
                headlineContent = {
                    Text(text =
                        stringResource(resource = Res.string.invite_teachers)
                    )
                }
            )
        }

        item {
            ListItem(
                leadingContent = {
                    Icon(imageVector = Icons.Filled.Check, contentDescription = null)
                },
                headlineContent = {
                    Text(text =
                        stringResource(resource = Res.string.accept_invite)
                    )
                }
            )
        }

        itemsIndexed(
            //dummy list
            listOf("Micky", "Mouse", "Bunny"),
            key = { index, name -> "teacher_$index" }
        ) { _, name ->
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .clickable { /* handle click */ },
                leadingContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        RespectAsyncImage(
                            uri = "", // Replace with image url
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                },
                headlineContent = {
                    Text(text = name)
                }
            )
        }

        item {
            Text(text =
                stringResource(resource = Res.string.students)
            )
        }

        item {
            ListItem(
                leadingContent = {
                    Icon(imageVector = Icons.Filled.Share, contentDescription = null)
                },
                headlineContent = {
                    Text(text =
                        stringResource(resource = Res.string.invite_students)
                    )
                }
            )
        }

        item {
            ListItem(
                leadingContent = {
                    Icon(imageVector = Icons.Filled.Check, contentDescription = null)
                },
                headlineContent = {
                    Text(text =
                        stringResource(resource = Res.string.accept_invite)
                        )
                }
            )
        }

        itemsIndexed(
            //dummy list
            listOf("Micky", "Mouse"),
            key = { index, name -> "student_$index" }
        ) { _, name ->
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .clickable { },
                leadingContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        RespectAsyncImage(
                            uri = "", // Replace with image url
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                },
                headlineContent = {
                    Text(text = name)
                }
            )
        }
    }
}
