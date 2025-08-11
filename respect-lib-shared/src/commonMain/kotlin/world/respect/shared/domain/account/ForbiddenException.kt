package world.respect.shared.domain.account

import world.respect.libutil.exceptionwithstatuscode.ExceptionWithStatusCode

class ForbiddenException(
    message: String?,
    cause: Throwable? = null
): Exception(message, cause), ExceptionWithStatusCode {
    override val statusCode: Int = 403
}
