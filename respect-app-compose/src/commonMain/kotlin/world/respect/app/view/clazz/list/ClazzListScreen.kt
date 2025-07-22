package world.respect.app.view.clazz.list

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
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
import world.respect.app.app.RespectAsyncImage
import world.respect.shared.viewmodel.clazz.list.ClazzListUiState
import world.respect.shared.viewmodel.clazz.list.ClazzListViewModel

@Composable
fun ClazzListScreen(
    viewModel: ClazzListViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    ClazzListScreen(
        uiState = uiState,
        onClickClazz = { viewModel.onClickClazz() },
        onClickFilter = { viewModel.onClickFilter(it) },
    )
}

@Composable
fun ClazzListScreen(
    uiState: ClazzListUiState,
    onClickClazz: () -> Unit,
    onClickFilter: (String) -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        val filterList = uiState.clazzFilter
        val selectedFilter = uiState.selectedFilterTitle ?: filterList.firstOrNull().orEmpty()

        if (filterList.isNotEmpty()) {
            var expanded by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp)
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
                        Text(text = selectedFilter)
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
                    filterList.forEach { filterItem ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = filterItem,
                                    fontSize = 14.sp
                                )
                            },
                            onClick = {
                                expanded = false
                                onClickFilter(filterItem)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            itemsIndexed(
                //dummy list
                uiState.clazzList,
                key = { index, name -> index }
            ) { index, name ->
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
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
                            text = name
                        )
                    }
                )
            }

        }
    }
}