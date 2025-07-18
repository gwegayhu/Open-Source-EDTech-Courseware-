package world.respect.app.app.dateofbirth

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate

@Composable
actual fun DateOfBirthSelector(
    date: LocalDate?,
    onDateChanged: (LocalDate) -> Unit,
    label: String,
    error: String?
) {
}