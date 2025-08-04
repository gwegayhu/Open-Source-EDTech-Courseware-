package world.respect.app.view.clazz.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import world.respect.app.app.RespectAsyncImage
import world.respect.app.components.RespectSortOption
import world.respect.shared.viewmodel.clazz.list.ClazzListUiState
import world.respect.shared.viewmodel.clazz.list.ClazzListViewModel
import world.respect.shared.viewmodel.clazz.list.ClazzListViewModel.Companion.NAME

@Composable
fun ClazzListScreen(
    viewModel: ClazzListViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    ClazzListScreen(
        uiState = uiState,
        onClickClazz = viewModel::onClickClazz,
        onClickSortOption = { viewModel.onClickSortOption(it) },
    )
}

@Composable
fun ClazzListScreen(
    uiState: ClazzListUiState,
    onClickClazz: () -> Unit,
    onClickSortOption: (String) -> Unit,
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            RespectSortOption(
                options = uiState.sortOptions,
                selectedOption = uiState.selectedSortOption ?: NAME,
                onOptionSelected = onClickSortOption,
                optionLabel = { it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            )
        }
        itemsIndexed(
            //dummy list
            uiState.oneRoasterClass,
            key = { index, clazz -> index }
        ) { index, clazz ->
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onClickClazz()
                    },

                leadingContent = {
                    //dummy data
                    val iconUrl = ""
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        iconUrl.also { icon ->
                            RespectAsyncImage(
                                uri = icon,
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(36.dp)
                            )
                        }
                    }
                },

                headlineContent = {
                    //dummy data
                    Text(
                        text = clazz.title
                    )
                }
            )
        }

    }
}