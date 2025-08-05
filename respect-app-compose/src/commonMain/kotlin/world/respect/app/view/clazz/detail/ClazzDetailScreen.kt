package world.respect.app.view.clazz.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import world.respect.app.components.RespectSortOption
import world.respect.app.components.RespectFilterChip
import world.respect.app.components.RespectPersonAvatar
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.add_teacher
import world.respect.shared.generated.resources.add_student
import world.respect.shared.generated.resources.description
import world.respect.shared.generated.resources.pending_invite
import world.respect.shared.generated.resources.students
import world.respect.shared.generated.resources.teachers
import world.respect.shared.viewmodel.clazz.detail.ClazzDetailUiState
import world.respect.shared.viewmodel.clazz.detail.ClazzDetailViewModel
import world.respect.shared.viewmodel.clazz.list.ClazzListViewModel.Companion.NAME

@Composable
fun ClazzDetailScreen(
    viewModel: ClazzDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    ClazzDetailScreen(
        uiState = uiState,
        onClickAcceptInvite = viewModel::onClickAcceptInvite,
        onClickInviteStudent = viewModel::onClickInviteStudent,
        onClickSortOption = { viewModel.onClickSortOption(it) },
        onSelectChip = { viewModel.onSelectChip(it) },
    )
}

@Composable
fun ClazzDetailScreen(
    uiState: ClazzDetailUiState,
    onClickAcceptInvite: () -> Unit,
    onClickInviteStudent: () -> Unit,
    onClickSortOption: (String) -> Unit,
    onSelectChip: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            RespectFilterChip(
                options = uiState.chipOptions,
                selectedOption = uiState.selectedChip,
                onOptionSelected = { onSelectChip(it) },
                optionLabel = { it }
            )

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
        item {
            ListItem(
                modifier = Modifier.clickable {
                    onClickAcceptInvite()
                },
                headlineContent = {
                    Text(
                        text = stringResource(
                            resource = Res.string.pending_invite
                        )
                    )
                }
            )
        }
        itemsIndexed(
            uiState.listOfTeachers,
            key = { index, name -> "invite_$index" }
        ) { _, name ->
            ListItem(
                modifier = Modifier.fillMaxWidth()
                    .clickable { /* handle click */ },
                leadingContent = {
                    RespectPersonAvatar(
                        name = name,
                        modifier = Modifier.size(40.dp),
                        fontScale = 1.0f
                    )
                },
                headlineContent = {
                    Text(text = name)
                },
                supportingContent = {
                    Text(text = stringResource(Res.string.description))
                },
                trailingContent = {
                    Row {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                        )
                        Icon(
                            imageVector = Icons.Outlined.Cancel,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            )
        }

        item {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                text =
                    stringResource(resource = Res.string.teachers)
            )
        }

        item {
            ListItem(
                leadingContent = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null
                    )
                },
                headlineContent = {
                    Text(
                        text =
                            stringResource(resource = Res.string.add_teacher)
                    )
                }
            )
        }


        itemsIndexed(
            //dummy list
            uiState.listOfTeachers,
            key = { index, name -> "teacher_$index" }
        ) { _, name ->
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* handle click */ },
                leadingContent = {
                    RespectPersonAvatar(
                        name = name,
                        modifier = Modifier.size(40.dp),
                        fontScale = 1.0f
                    )
                },
                headlineContent = {
                    Text(text = name)
                }
            )
        }

        item {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                text = stringResource(resource = Res.string.students)
            )
        }

        item {
            ListItem(
                modifier = Modifier.clickable {
                    onClickInviteStudent()
                },
                leadingContent = {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                },
                headlineContent = {
                    Text(
                        text =
                            stringResource(resource = Res.string.add_student)
                    )
                }
            )
        }

        itemsIndexed(
            //dummy list
            uiState.listOfTeachers,
            key = { index, name -> "student_$index" }
        ) { _, name ->
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { },
                leadingContent = {
                    RespectPersonAvatar(
                        name = name,
                        modifier = Modifier.size(40.dp),
                        fontScale = 1.0f
                    )
                },
                headlineContent = {
                    Text(
                        text = name
                    )
                }
            )
        }
    }
}

