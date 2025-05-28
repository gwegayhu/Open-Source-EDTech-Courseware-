package world.respect.domain.opds.validator

import com.networknt.schema.InputFormat
import world.respect.domain.validator.ValidatorMessage
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import kotlinx.serialization.json.Json
import world.respect.domain.opds.model.OpdsFeed
import world.respect.domain.opds.model.OpdsPublication
import java.net.URI

class OpdsValidatorUseCase(
    private val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    },
) {

    private val factory by lazy {
        JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012)
    }

    private val schema by lazy {
        factory.getSchema(URI("https://drafts.opds.io/schema/feed.schema.json")).also {
            it.initializeValidators()
        }
    }

    operator fun invoke(
        url: String,
        recursive: Boolean,
        visitedFeeds: MutableList<String>,
        expectedType: String = OpdsFeed.MEDIA_TYPE,
    ): List<ValidatorMessage> {
        val validationMessages = mutableListOf<ValidatorMessage>()

        try {
            println("Loading $url ...")

            val urlUri = URI(url)
            val text = urlUri.toURL().readText()
            visitedFeeds.add(url)

            val messages = schema.validate(text, InputFormat.JSON)
            validationMessages.addAll(
                messages.map {
                    ValidatorMessage(
                        isError = true,
                        sourceUri = url,
                        message = it.toString()
                    )
                }
            )

            when(expectedType) {
                OpdsFeed.MEDIA_TYPE -> {
                    val opdsFeed = json.decodeFromString<OpdsFeed>(text)
                    if(recursive) {
                        val allLinks = opdsFeed.links + (opdsFeed.navigation ?: emptyList()) +
                                (opdsFeed.facets?.flatMap { it.links } ?: emptyList() ?: emptyList()) +
                                (opdsFeed.groups?.flatMap { it.links ?: emptyList() } ?: emptyList()) +
                                (opdsFeed.publications?.flatMap { it.links } ?: emptyList())

                        allLinks.forEach { link ->
                            val linkType = link.type
                            if(linkType == null || !linkType.startsWith("application/opds"))
                                return@forEach

                            val linkUrl = urlUri.resolve(link.href).toURL().toString()
                            if(!visitedFeeds.contains(linkUrl)) {
                                validationMessages += invoke(
                                    url = linkUrl,
                                    recursive = recursive,
                                    visitedFeeds = visitedFeeds,
                                    expectedType = linkType,
                                )
                            }
                        }
                    }
                }

                OpdsPublication.MEDIA_TYPE -> {
                    //As per the spec (section 5.1) https://drafts.opds.io/opds-2.0.html#51-opds-publication
                    // See readium doc: https://readium.org/webpub-manifest/
                }
            }


        }catch(e : Throwable) {
            validationMessages += ValidatorMessage(
                isError = true,
                sourceUri = url,
                message = "Error processing $url : ${e.message}"
            )
        }

        return validationMessages
    }


}