package world.respect.shared.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

fun LocalDate.displayStr(): String {
    return "$day/${month.number}/$year"
}
