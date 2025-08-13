package world.respect.shared.domain.account

import world.respect.libutil.util.throwable.ExceptionWithHttpStatusCode

class ForbiddenException(
    message: String?,
    cause: Throwable? = null
): Exception(message, cause), ExceptionWithHttpStatusCode {
    override val statusCode: Int = 403
}
