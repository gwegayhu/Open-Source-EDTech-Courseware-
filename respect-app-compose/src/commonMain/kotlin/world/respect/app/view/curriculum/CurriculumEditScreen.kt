package world.respect.app.view.curriculum

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background

data class Curriculum(
    val id: String,
    val name: String,
    val description: String,
    val isActive: Boolean
)

fun Curriculum?.getDisplayName(newCurriculumText: String): String {
    val curriculum = this
    if (curriculum != null) {
        val curriculumName = curriculum.name
        val curriculumId = curriculum.id

        return if (curriculumName.isNotEmpty() && curriculumId.isNotEmpty()) {
            "$curriculumName - $curriculumId"
        } else curriculumName.ifEmpty {
            curriculumId.ifEmpty {
                newCurriculumText
            }
        }
    } else {
        return newCurriculumText
    }
}

data class CurriculumEditUiState(
    val curriculum: Curriculum? = null,
    val name: String = "",
    val id: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val isEditMode: Boolean = false,
    val nameError: String? = null,
    val idError: String? = null,
    val descriptionError: String? = null,
    val error: String? = null
) {
    val isValid: Boolean
        get() {
            val nameValue = name
            val idValue = id
            val descriptionValue = description
            val nameErrorValue = nameError
            val idErrorValue = idError
            val descriptionErrorValue = descriptionError

            return nameValue.isNotBlank() && idValue.isNotBlank() && descriptionValue.isNotBlank() &&
                    nameErrorValue == null && idErrorValue == null && descriptionErrorValue == null
        }
}

@Composable
fun CurriculumEditScreenWrapper() {
    var uiState by remember {
        mutableStateOf(
            CurriculumEditUiState(
                isEditMode = false
            )
        )
    }
    val emptyName = stringResource(Res.string.empty_name)
    val emptyId = stringResource(Res.string.empty_id)
    val emptyDescription = stringResource(Res.string.empty_description)
    val nameRequiredError = stringResource(Res.string.name_required_error)
    val idRequiredError = stringResource(Res.string.id_required_error)
    val descriptionRequiredError = stringResource(Res.string.description_required_error)

    CurriculumEditScreen(
        uiState = uiState,
        onNameChange = { newName ->
            uiState = uiState.copy(
                name = newName,
                nameError = null
            )
        },
        onIdChange = { newId ->
            uiState = uiState.copy(
                id = newId,
                idError = null
            )
        },
        onDescriptionChange = { newDesc ->
            uiState = uiState.copy(
                description = newDesc,
                descriptionError = null
            )
        },
        onBackClick = { /* navController.popBackStack() */ },
        onSaveClick = {
            val currentState = uiState
            if (currentState.isValid) {
                uiState = uiState.copy(
                    name = emptyName,
                    id = emptyId,
                    description = emptyDescription
                )
            } else {
                val nameError = if (currentState.name.isBlank()) {
                    nameRequiredError
                } else null

                val idError = if (currentState.id.isBlank()) {
                    idRequiredError
                } else null

                val descriptionError = if (currentState.description.isBlank()) {
                    descriptionRequiredError
                } else null

                uiState = uiState.copy(
                    nameError = nameError,
                    idError = idError,
                    descriptionError = descriptionError
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurriculumEditScreen(
    uiState: CurriculumEditUiState = CurriculumEditUiState(),
    onNameChange: (String) -> Unit = {},
    onIdChange: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = stringResource(Res.string.edit_curriculum),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            OutlinedButton(
                onClick = onSaveClick,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.height(32.dp)
            ) {
                Text(
                    text = stringResource(Res.string.save),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                label = { Text(stringResource(Res.string.name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.nameError != null,
                supportingText = {
                    val nameErrorText = uiState.nameError
                    if (nameErrorText != null) {
                        Text(nameErrorText)
                    }
                }
            )

            OutlinedTextField(
                value = uiState.id,
                onValueChange = onIdChange,
                label = { Text(stringResource(Res.string.id)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.idError != null,
                supportingText = {
                    val idErrorText = uiState.idError
                    if (idErrorText != null) {
                        Text(idErrorText)
                    }
                }
            )

            OutlinedTextField(
                value = uiState.description,
                onValueChange = onDescriptionChange,
                label = { Text(stringResource(Res.string.description)) },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.descriptionError != null,
                supportingText = {
                    val descriptionErrorText = uiState.descriptionError
                    if (descriptionErrorText != null) {
                        Text(descriptionErrorText)
                    }
                }
            )
        }

        val isLoadingState = uiState.isLoading
        if (isLoadingState) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        val errorMessage = uiState.error
        if (errorMessage != null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}