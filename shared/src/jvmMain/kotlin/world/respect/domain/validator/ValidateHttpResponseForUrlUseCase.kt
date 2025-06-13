package world.respect.domain.validator

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.prepareGet
import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.remaining
import io.ktor.utils.io.exhausted
import io.ktor.utils.io.readRemaining
import kotlinx.io.asSink
import java.io.OutputStream

/**
 * Validate an HTTP response for a given URL. Can be used to check that the URL:
 * a) Returns a 200/success status code
 * b) Returns a content-type that is acceptable
 * c) Has required headers
 * d) Supports cache validation (e.g. using if-none-match if-modified-since).
 */
class ValidateHttpResponseForUrlUseCase(
    private val httpClient: HttpClient,
) {

    data class ValidateHttpResponseForUrlResult(
        val messages: List<ValidatorMessage>,
        val responseHeaders: Headers?,
        val statusCode: HttpStatusCode?,
    )

    private class DiscardOutputStream: OutputStream() {
        override fun write(p0: ByteArray) {
            //do nothing
        }

        override fun write(p0: ByteArray, p1: Int, p2: Int) {
            //do nothing
        }

        override fun write(p0: Int) {
            //do nothing
        }
    }

    data class ValidationOptions(
        val acceptableMimeTypes: List<String> = emptyList(),
    )

    /**
     * Validate the HTTP response for a given URL (e.g. ensure it returns 200 OK, has required
     * headers, mime type is in acceptable list, etc)
     *
     * @param url url to validate
     * @param referer the referer e.g. the URL from which this was requested - this is shown as the
     *        error source in the reporter.
     */
    suspend operator fun invoke(
        url: String,
        referer: String,
        reporter: ValidatorReporter,
        options: ValidationOptions = DEFAULT_VALIDATION_OPTS,
    ): ValidateHttpResponseForUrlResult  {
        val sink = DiscardOutputStream().asSink()
        val validatorMessages = mutableListOf<ValidatorMessage>()

        val linkToStr = "Link to $url"
        var responseHeaders: Headers? = null
        var httpStatusCode: HttpStatusCode? = null

        try {
            //As per https://ktor.io/docs/client-responses.html#streaming
            httpClient.prepareGet(url){
                expectSuccess = false
            }.execute { response ->
                responseHeaders = response.headers
                httpStatusCode = response.status

                val contentType = response.headers["content-type"]?.substringBefore(";")
                if(options.acceptableMimeTypes.isNotEmpty() &&
                    contentType !in options.acceptableMimeTypes
                ) {
                    validatorMessages += reporter.addMessage(
                        ValidatorMessage(
                            level = ValidatorMessage.Level.ERROR,
                            sourceUri = referer,
                            message = "$linkToStr: Response provides unacceptable mime type: $contentType. Should be: ${options.acceptableMimeTypes.joinToString()}"
                        )
                    )
                }

                if(response.status.value != 200) {
                    validatorMessages += reporter.addMessage(
                        ValidatorMessage(
                            level = ValidatorMessage.Level.ERROR,
                            sourceUri = referer,
                            message = "$linkToStr: Response status code not HTTP OK/200. Got: ${response.status.value}"
                        )
                    )
                }

                val channel: ByteReadChannel = response.body()
                var count = 0L
                while(!channel.exhausted()) {
                    val chunk = channel.readRemaining()
                    count += chunk.remaining
                    chunk.transferTo(sink)
                }
            }
        }catch(e: Throwable) {
            validatorMessages += reporter.addMessage(ValidatorMessage.fromException(url, e))
        }

        return ValidateHttpResponseForUrlResult(
            messages = validatorMessages,
            responseHeaders = responseHeaders,
            statusCode = httpStatusCode,
        )
    }

    companion object {

        val DEFAULT_VALIDATION_OPTS = ValidationOptions()

    }

}