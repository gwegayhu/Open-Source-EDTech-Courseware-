package world.respect.app.view.clazz.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import world.respect.shared.viewmodel.clazz.edit.ClazzEditViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.class_name_label
import world.respect.shared.generated.resources.description
import world.respect.shared.generated.resources.end_date_label
import world.respect.shared.generated.resources.start_date_label
import world.respect.shared.viewmodel.clazz.edit.ClazzEditUiState
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import world.respect.app.components.defaultItemPadding
import world.respect.datalayer.oneroster.rostering.model.OneRosterClass
import kotlin.time.ExperimentalTime


@Composable
fun ClazzEditScreen(
    viewModel: ClazzEditViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    ClazzEditScreen(
        uiState = uiState,
        onClazzChanged = viewModel::onClazzChanged,
    )
}

@OptIn(ExperimentalTime::class)
@Composable
fun ClazzEditScreen(
    uiState: ClazzEditUiState,
    onClazzChanged: (OneRosterClass?) -> Unit = {},

    ) {
    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        OutlinedTextField(
            value = uiState.entity?.title ?: "",
            label = {
                Text(
                    text = stringResource(Res.string.class_name_label)
                )
            },
            onValueChange = {
                onClazzChanged(uiState.entity?.copy(
                    title = it
                ))
            },

            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
            modifier = Modifier.fillMaxWidth()
                .defaultItemPadding()
        )

        OutlinedTextField(
            value = uiState.description,
            onValueChange = {
                onClazzChanged
            },
            label = {
                Text(
                    text = stringResource(Res.string.description)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
            modifier = Modifier.fillMaxWidth()
                .defaultItemPadding()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = uiState.startDate,
                onValueChange = {
                    onClazzChanged
                },
                label = {
                    Text(
                        text = stringResource(Res.string.start_date_label)
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = stringResource(Res.string.start_date_label),
                    )
                },
                modifier = Modifier.weight(1f)
                    .defaultItemPadding()
            )

            OutlinedTextField(
                value = uiState.endDate,
                onValueChange = {
                    onClazzChanged
                },
                label = {
                    Text(
                        text = stringResource(Res.string.end_date_label)
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = stringResource(Res.string.start_date_label),
                    )
                },
                modifier = Modifier.weight(1f)
                    .defaultItemPadding()
            )
        }
    }
}
