package world.respect.libutil.exceptionwithstatuscode

/**
 * Exception wrapper that includes an http status code e.g. so when an exception is thrown,
 * the server can respond with the appropriate status code error (eg unauthorized, not found, etc)
 */
interface ExceptionWithStatusCode {
    val statusCode: Int
}

class StatusCodeExceptionWrapper(
    override val statusCode: Int,
    message: String?,
    cause: Throwable? = null
): Exception( message, cause), ExceptionWithStatusCode


fun Throwable.withStatusCode(statusCode: Int): ExceptionWithStatusCode {
    return StatusCodeExceptionWrapper(statusCode, message ?: "", cause)
}

