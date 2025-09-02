package world.respect.app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import world.respect.shared.generated.resources.Res
import world.respect.shared.util.SortOrderOption
import world.respect.shared.generated.resources.sort_by
import world.respect.shared.util.description

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RespectSortOptionsBottomSheet(
    sortOptions: List<SortOrderOption> = emptyList(),
    onClickSortOption: (SortOrderOption) -> Unit = { },
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.headlineSmall,
            text = stringResource(Res.string.sort_by)
        )

        HorizontalDivider(thickness = 1.dp)

        Column(
            Modifier.verticalScroll(
                state = rememberScrollState()
            ).fillMaxSize()
        ) {
            sortOptions.forEach { sortOption ->
                RespectBottomSheetOption(
                    modifier = Modifier.clickable {
                        onDismissRequest()
                        onClickSortOption(sortOption)
                    },
                    headlineContent = {
                        Text(sortOption.description())
                    },
                )
            }
        }
    }

}