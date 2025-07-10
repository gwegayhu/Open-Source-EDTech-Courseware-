package world.respect.datasource.db.shared.ext

fun <T> List<T>.takeIfNotEmpty(): List<T>? {
    return takeIf { it.isNotEmpty() }
}
