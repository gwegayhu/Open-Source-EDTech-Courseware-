package world.respect.shared.util.exception

import world.respect.libutil.ext.getCauseOfType
import world.respect.shared.resources.UiText

/**
 * An exception that has a (potentially localizable) UiText associated with it. This makes it easier
 * for ViewModels to show an appropriate error message to the user.
 */
interface ExceptionWithUiMessage {
    val uiText: UiText
}

class ExceptionUiMessageWrapper(
    cause: Throwable?,
    message: String?,
    override val uiText: UiText
): Exception(message, cause), ExceptionWithUiMessage

fun Throwable.getUiText(): UiText? {
    return getCauseOfType<ExceptionWithUiMessage>()?.uiText
}
