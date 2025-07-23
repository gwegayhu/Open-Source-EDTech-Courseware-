package world.respect.app.view.clazz.student

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.QrCode
import world.respect.shared.generated.resources.copy_invite_code
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.share_qr_code
import world.respect.shared.generated.resources.share_with_link_or_qr_code
import world.respect.shared.viewmodel.clazz.student.AddStudentUIState
import world.respect.shared.viewmodel.clazz.student.StudentViewModel

@Composable
fun StudentScreen(
    viewModel: StudentViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    StudentScreen(
        uiState = uiState
    )
}

@Composable
fun StudentScreen(
    uiState: AddStudentUIState
) {

    Column {
        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Filled.CopyAll,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            },
            headlineContent = {
                Text(text = stringResource(Res.string.copy_invite_code))
            },
        )
        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Filled.Link,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            },
            headlineContent = {
                Text(text = stringResource(Res.string.share_with_link_or_qr_code))
            },
        )
        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Filled.QrCode,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            },
            headlineContent = {
                Text(text = stringResource(Res.string.share_qr_code))
            },
        )


    }
}