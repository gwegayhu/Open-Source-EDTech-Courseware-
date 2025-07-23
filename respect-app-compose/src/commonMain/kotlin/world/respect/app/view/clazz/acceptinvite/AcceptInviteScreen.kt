package world.respect.app.view.clazz.acceptinvite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import world.respect.app.app.RespectAsyncImage
import world.respect.shared.viewmodel.clazz.acceptinvite.AcceptInviteUiState
import world.respect.shared.viewmodel.clazz.acceptinvite.AcceptInviteViewModel

@Composable
fun AcceptInviteScreen(
    viewModel: AcceptInviteViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    AcceptInviteScreen(
        uiState = uiState
    )
}

@Composable
fun AcceptInviteScreen(
    uiState: AcceptInviteUiState
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        itemsIndexed(
            listOf("Micky", "Mouse", "Bunny"),
            key = { index, name -> "invite_$index" }
        ) { _, name ->
            ListItem(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)
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
                },
                supportingContent = {
                    Text(text = "Description")
                },
                trailingContent = {
                    Row {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }

    }

}