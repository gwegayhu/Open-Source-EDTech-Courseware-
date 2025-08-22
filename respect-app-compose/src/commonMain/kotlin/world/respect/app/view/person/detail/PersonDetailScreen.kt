package world.respect.app.view.person.detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import world.respect.datalayer.ext.dataOrNull
import world.respect.shared.util.ext.fullName
import world.respect.shared.viewmodel.person.detail.PersonDetailUiState
import world.respect.shared.viewmodel.person.detail.PersonDetailViewModel

@Composable
fun PersonDetailScreen(
    viewModel: PersonDetailViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    PersonDetailScreen(uiState)
}

@Composable
fun PersonDetailScreen(
    uiState: PersonDetailUiState,
) {
    Text(uiState.person.dataOrNull()?.fullName() ?: "")
}
