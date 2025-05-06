package world.respect.app.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kodein.di.compose.localDI
import world.respect.app.viewmodel.AppListScreenViewModel


@Composable
fun AppListScreen() {
    val di = localDI()
    val viewModel = AppListScreenViewModel(di)
    val uiState by viewModel.uiState.collectAsState()

   // var selectedItem by remember { mutableStateOf<AppListModel?>(null) }
    var selectedItems by remember { mutableStateOf(setOf<String>()) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("App Link", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Link,
                contentDescription = "Link Icon",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add from link", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(uiState.appListData) { app ->
                val isSelected = selectedItems.contains(app.id)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    /*RadioButton(
                        selected = selectedItem?.id == app.id,
                        onClick = { selectedItem = app }
                    )*/
                    RadioButton(
                        selected = isSelected,
                        onClick = {
                            selectedItems = if (isSelected) {
                                selectedItems - app.id
                            } else {
                                selectedItems + app.id
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = app.title, fontSize = 16.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(text = app.category, fontSize = 12.sp)
                            Text(text = app.ageRange, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
