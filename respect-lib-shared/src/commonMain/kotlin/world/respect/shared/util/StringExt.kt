package world.respect.shared.util

fun String.firstNonWhiteSpaceChar(): Char? {
    val index = indexOfFirst { !it.isWhitespace() }
    return if(index != -1)
        this[index]
    else
        null
}

fun String.initial(): String {
    return firstNonWhiteSpaceChar()?.uppercase() ?: ""
}
