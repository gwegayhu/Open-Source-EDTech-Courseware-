package world.respect.app.view.curriculum

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurriculumEditScreen(
    name: String = "",
    id: String = "",
    description: String = "",
    isEditMode: Boolean = true,
    onNameChange: (String) -> Unit = {},
    onIdChange: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = if (isEditMode) stringResource(Res.string.edit_curriculum) else stringResource(Res.string.add_curriculum),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            actions = {
                TextButton(onClick = onSaveClick) {
                    Text(stringResource(Res.string.save))
                }
            }
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text(stringResource(Res.string.name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = id,
                onValueChange = onIdChange,
                label = { Text(stringResource(Res.string.id)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text(stringResource(Res.string.description)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}