package world.respect.datalayer.respect.model

import io.ktor.http.Url
import kotlinx.serialization.Serializable

/**
 * A RESPECT Server provides a set of API endpoints: xAPI, OneRoster, and possibly RESPECT extensions.
 *
 * @property xapi URL to xAPI endpoint
 * @property oneRoster URL to OneRoster endpoint
 * @property respectExt URL to Respect extensions endpoint (if available). Required for invites etc.
 */
@Serializable
class RespectServerUrls(
    val xapi: Url,
    val oneRoster: Url,
    val respectExt: Url?
)
