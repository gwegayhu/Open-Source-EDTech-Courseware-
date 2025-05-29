package world.respect.domain.validator

import world.respect.domain.opds.model.ReadiumLink

/**
 * Validate an OPDS Link - this can be :
 *   An OPDS feed or publication as per https://drafts.opds.io/opds-2.0#appendix-a-json-schema .
 *   Images or resources
 */
interface OpdsLinkValidatorUseCase {

    /**
     * @param link ReadiumLink - the href and type will be used. If the type is not specified this
     *        will currently default to application/opds+json
     * @param baseUrl absolute base url that will be used to resolve the link (e.g. relative links)
     * @param visitedUrls a list of URLs already visited; used to avoid validating the same url more
     *        than once
     * @param followLinks if true recursively follow links where the type being checked has
     *        links e.g. Opds Feeds.
     * @return a list of ValidatorMessage containing any errors and warnings (if any).
     */
    operator fun invoke(
        link: ReadiumLink,
        baseUrl: String,
        visitedUrls: MutableList<String>,
        followLinks: Boolean,
    ): List<ValidatorMessage>

}