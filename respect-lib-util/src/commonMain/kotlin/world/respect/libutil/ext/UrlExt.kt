package world.respect.libutil.ext

import io.ktor.http.Url
import io.ktor.http.toURI

/**
 * Resolve an href (that could be absolute or relative)
 *
 * @receiver a KTOR Url (an absolute URL)
 */
fun Url.resolve(href: String): Url {
    return Url(toURI().resolve(href).toString())
}

/**
 * Sanitize a URL for use in a filename (e.g. a database name). Replaces anything that is not
 * a letter, digit, _, or - with an underscore
 */
fun Url.sanitizedForFilename(): String {
    return toString().map {
        if(it.isLetter() || it.isDigit() || it == '_' || it == '-') {
            it
        }else {
            '_'
        }
    }.toCharArray().concatToString()
}
