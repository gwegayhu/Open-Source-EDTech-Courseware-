package world.respect.domain.validator

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.prepareGet
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

    suspend operator fun invoke(
        url: String,
        referer: String?,
        reporter: ValidatorReporter,
        options: ValidationOptions = DEFAULT_VALIDATION_OPTS,
    )  {
        val sink = DiscardOutputStream().asSink()

        val linkFromStr = referer?.let {
            "(Linked from $referer)"
        } ?: ""

        try {
            //As per https://ktor.io/docs/client-responses.html#streaming
            httpClient.prepareGet(url).execute { response ->response.headers["content-type"]
                val contentType = response.headers["content-type"]?.substringBefore(";")
                if(response.headers["content-type"] !in options.acceptableMimeTypes) {
                    reporter.addMessage(
                        ValidatorMessage(
                            isError = true,
                            sourceUri = url,
                            "Wrong mime type: $contentType not ${options.acceptableMimeTypes.joinToString()}" +
                                    " $linkFromStr"
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
            reporter.addMessage(ValidatorMessage.fromException(url, e))
        }
    }

    companion object {

        val DEFAULT_VALIDATION_OPTS = ValidationOptions()

    }

}