package world.respect.shared.domain.account

import world.respect.libutil.util.throwable.ExceptionWithHttpStatusCode

/**
 * Forbidden exception: as per hte 403 status code the server is saying that the identity is
 * verified, but the request is not allowed.
 */
class ForbiddenException(
    message: String?,
    cause: Throwable? = null
): Exception(message, cause), ExceptionWithHttpStatusCode {
    override val statusCode: Int = 403
}
