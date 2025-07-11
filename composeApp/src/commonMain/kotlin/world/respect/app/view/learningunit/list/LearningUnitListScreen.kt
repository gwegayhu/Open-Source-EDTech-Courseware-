package world.respect.app.view.learningunit.list

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ustadmobile.libuicompose.theme.black
import org.jetbrains.compose.resources.stringResource
import respect.composeapp.generated.resources.Res
import respect.composeapp.generated.resources.clazz
import respect.composeapp.generated.resources.duration
import world.respect.app.app.RespectAsyncImage
import world.respect.app.appstate.getTitle
import world.respect.app.appstate.toDisplayString
import world.respect.app.viewmodel.learningunit.list.LearningUnitListUiState
import world.respect.app.viewmodel.learningunit.list.LearningUnitListViewModel
import world.respect.datasource.opds.model.OpdsPublication
import world.respect.datasource.opds.model.ReadiumLink

@Composable
fun LearningUnitListScreen(
    viewModel: LearningUnitListViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LearningUnitListScreen(
        uiState = uiState,
        onClickLearningUnit = { viewModel.onClickLearningUnit(it) },
        onClickFilter = { viewModel.onClickFilter(it) },
        onClickPublication = { viewModel.onClickPublication(it) },
        onClickNavigation = { viewModel.onClickNavigation(it) }
    )
}

@Composable
fun LearningUnitListScreen(
    uiState: LearningUnitListUiState,
    onClickLearningUnit: (String) -> Unit,
    onClickFilter: (String) -> Unit,
    onClickPublication: (OpdsPublication) -> Unit,
    onClickNavigation: (ReadiumLink) -> Unit

) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        uiState.lessonFilter.firstOrNull()?.let { facet ->
            var expanded by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .border(1.dp, black, shape = RoundedCornerShape(6.dp))
                        .clip(CircleShape)
                        .clickable { expanded = true }
                        .padding(horizontal = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = uiState.selectedFilterTitle ?: (facet.metadata?.title ?: ""))
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            modifier = Modifier.padding(6.dp),
                            contentDescription = null
                        )
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = facet.metadata?.title ?: "",
                                fontSize = 14.sp
                            )
                        },
                        onClick = {},
                        enabled = false
                    )

                    facet.links.forEach { link ->
                        DropdownMenuItem(
                            text = { Text(link.title ?: "") },
                            onClick = {
                                onClickFilter(link.title ?: "")
                                expanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }


        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            itemsIndexed(
                items = uiState.navigation,
                key = { index, navigation ->
                    navigation.href
                }
            ) { index, navigation ->
                NavigationListItem(
                    navigation,
                    onClickNavigation = {
                        onClickNavigation(navigation)
                    }
                )
            }

            itemsIndexed(
                items = uiState.publications,
                key = { index, publications ->
                    publications.metadata.identifier.toString()
                }
            ) { index, publication ->
                PublicationListItem(
                    publication,
                    onClickPublication = {
                        onClickPublication(publication)
                    }
                )
            }

            uiState.group.forEach { group ->
                item {
                    ListItem(
                        headlineContent = {
                            Text(
                                text = group.metadata.title,
                            )
                        }
                    )
                }

                itemsIndexed(
                    items = group.navigation ?: emptyList(),
                    key = { index, navigation ->
                        navigation.href
                    }
                ) { index, navigation ->
                    NavigationListItem(
                        navigation,
                        onClickNavigation = {
                            onClickNavigation(navigation)
                        }
                    )
                }
                itemsIndexed(
                    items = group.publications ?: emptyList(),
                    key = { index, publication ->
                        publication.metadata.identifier.toString()
                    }
                ) { index, publication ->
                    PublicationListItem(
                        publication,
                        onClickPublication = {
                            onClickPublication(publication)
                        }
                    )
                }

            }
        }
    }

}

@Composable
fun NavigationListItem(
    navigation: ReadiumLink,
    onClickNavigation: (ReadiumLink) -> Unit
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClickNavigation(navigation)
            },

        leadingContent = {
            val iconUrl = navigation.alternate?.find {
                it.rel?.contains("icon") == true
            }?.href

            RespectAsyncImage(
                uri = iconUrl ?: "",
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline,
                        CircleShape
                    )
            )
        },

        headlineContent = {
            Text(
                text = navigation.title.toString()
            )
        },

        supportingContent = {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = stringResource(Res.string.clazz),
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = navigation.type.toString()
                    )
                    Text(
                        text = "${stringResource(Res.string.duration)} -" +
                                " ${navigation.duration}"
                    )
                }
            }
        },

        trailingContent = {
            Icon(
                imageVector = Icons.Filled.ArrowCircleDown,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    )
}

@Composable
fun PublicationListItem(
    publication: OpdsPublication, onClickPublication: (OpdsPublication) -> Unit
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickPublication(publication) },

        leadingContent = {
            RespectAsyncImage(
                uri = publication.images?.firstOrNull()?.href.toString(),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline,
                        CircleShape
                    )
            )
        },

        headlineContent = {
            Text(
                text = publication.metadata.title.getTitle(),
            )
        },

        supportingContent = {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = stringResource(Res.string.clazz),
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = publication.metadata.subject
                            ?.joinToString(", ") { it.toDisplayString() }
                            ?: " ",
                    )
                    Text(
                        text = "${stringResource(Res.string.duration)} - " +
                                "${publication.metadata.duration}",
                    )
                }
            }
        },

        trailingContent = {
            Icon(
                imageVector = Icons.Filled.ArrowCircleDown,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    )
}