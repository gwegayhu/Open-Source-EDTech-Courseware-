package world.respect.domain.opds.validator

import io.ktor.http.Url
import io.ktor.util.toMap
import org.jsoup.Jsoup
import world.respect.domain.opds.model.OpdsPublication
import world.respect.domain.opds.model.toStringMap
import world.respect.domain.validator.HttpLinkHeader
import world.respect.domain.validator.ValidateHttpResponseForUrlUseCase
import world.respect.domain.validator.ValidatorMessage
import world.respect.domain.validator.ValidatorReporter
import java.net.URI

/**
 * Validate an Opds Publication Element.
 *
 * This use case operates an OpdsPublication instance and is therefor used by both the
 * OpdsFeedValidator and the OpdsPublicationValidator.
 */
class ValidateOpdsPublicationUseCase(
    private val validateHttpResponseForUrlUseCase: ValidateHttpResponseForUrlUseCase,
) {

    /**
     * @param publication OpdsPublication to be validated
     * @param url the URL of the OPDS JSON loaded (used to resolve links)
     * @param reporter ValidatorReporter
     */
    suspend operator fun invoke(
        publication: OpdsPublication,
        url: String,
        reporter: ValidatorReporter,
    ) {
        val acquisitionLinks = publication.links.filter { link ->
            val linkType = link.type?.substringBefore(";")
            link.rel?.any { it.startsWith("http://opds-spec.org/acquisition") } == true &&
                    linkType in LEARNING_UNIT_MIME_TYPES
        }

        val publicationTitleAndId = buildString {
            append("title \"")
            append(publication.metadata.title.toStringMap().values.firstOrNull() ?: "[Unknown]")
            append("\"")
            publication.metadata.identifier?.also {
                append(" and identifier: $it")
            }
        }

        //Check publication has acceptable acquisition link element
        if(acquisitionLinks.isEmpty()) {
            reporter.addMessage(
                ValidatorMessage(
                    isError = true,
                    sourceUri = url,
                    message = buildString {
                        append("No suitable acquisition links found for publication: ")
                        append(publicationTitleAndId)
                        append(". Acquisition link MUST use rel starting with http://opds-spec.org/acquisition " +
                                "AND use one of the following types: ${LEARNING_UNIT_MIME_TYPES.joinToString()}")
                    }
                )
            )
        }

        //Check learning resource id (URL) does not contain a # (that would not go to server) or
        //use any reserved query parameters
        acquisitionLinks.forEach { link ->
            val learningResourceIdUrl = URI(url).resolve(link.href)
            if(learningResourceIdUrl.toString().contains("#")) {
                reporter.addMessage(
                    ValidatorMessage(
                        isError = true,
                        sourceUri = url,
                        message = buildString {
                            append("Learning Resource ID URL ($learningResourceIdUrl) contains a #" )
                            append(publicationTitleAndId)
                        }
                    )
                )
            }

            val queryParamNames = Url((learningResourceIdUrl.toString())).parameters.toMap().keys
            val reservedQueryParamNamesUsed = queryParamNames.filter {
                it in LEARNING_UNIT_RESERVED_PARAMS
            }
            if(reservedQueryParamNamesUsed.isNotEmpty()) {
                reporter.addMessage(
                    ValidatorMessage(
                        isError = true,
                        sourceUri = url,
                        message = buildString {
                            append("Learning Resource ID URL ($learningResourceIdUrl) for publication ")
                            append(publicationTitleAndId)
                            append(" contains reserved query parameters: ${reservedQueryParamNamesUsed.joinToString()}")
                        }
                    )
                )
            }

            //Check learning resource URL results in an HTTP 200 OK response with acceptable mime type
            val responseValidationResult = validateHttpResponseForUrlUseCase(
                url = learningResourceIdUrl.toString(),
                referer = url,
                reporter = reporter,
                options = ValidateHttpResponseForUrlUseCase.ValidationOptions(
                    acceptableMimeTypes = LEARNING_UNIT_MIME_TYPES
                )
            )

            //Try to discover manifest link as per the Readium publication manifest specification
            val manifestHeaderLink = responseValidationResult.responseHeaders?.getAll("Link")
                ?.firstNotNullOf { headerVal ->
                    HttpLinkHeader.parseHeaderValue(headerVal).links.firstOrNull {
                        it.params["rel"]?.contains("manifest") == true &&
                            it.params["type"]?.substringBefore(";")?.trim() == "application/webpub+json"
                    }
                }

            val jsoupDoc = try {
                Jsoup.connect(learningResourceIdUrl.toString()).get()
            }catch(e: Throwable) {
                reporter.addMessage(
                    ValidatorMessage(
                        isError = true,
                        sourceUri = url,
                        message = buildString {
                            append("Could not load or parse learning resource ID: $learningResourceIdUrl")
                            append(" URL MUST return an HTTP 200/OK response and MUST contain valid HTML or XHTML")
                        }
                    )
                )
                null
            }

            val manifestUrl = manifestHeaderLink?.uriRef?.let {
                learningResourceIdUrl.resolve(URI(it))
            } ?: jsoupDoc?.select("link")?.filter { node ->
                node.attr("rel")
                    .ifEmpty { null }
                    ?.split(Regex("\\W"))
                    ?.contains("manifest") == true &&
                        node.attr("type")
                            .ifEmpty { null }
                            ?.substringBefore(";")
                            ?.trim() == "application/webpub+json"
            }?.firstOrNull()?.absUrl("href")

            if(manifestUrl == null) {
                reporter.addMessage(
                    ValidatorMessage(
                        isError = true,
                        sourceUri = url,
                        message = buildString {
                            append("Manifest not discovered for learning resource ID URL: $learningResourceIdUrl .")
                            append("Readium publication manifest must be discoverable as per https://github.com/readium/webpub-manifest?tab=readme-ov-file#5-discovering-a-manifest")
                        }
                    )
                )
            }
        }
    }

    companion object {
        val LEARNING_UNIT_MIME_TYPES = listOf("text/html", "application/xml", "application/html+xml",
            "application/html")

        val LEARNING_UNIT_RESERVED_PARAMS = listOf(
            "respectLaunchVersion", "auth", "given_name", "locale", "endpoint",
            "endpoint_oneroster", "actor", "registration", "activity_id"
        )

    }

}