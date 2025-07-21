package world.respect.datalayer.respect.model

import io.ktor.http.Url
import kotlinx.serialization.Serializable

/**
 * A RESPECT Realm (a logical grouping of networked resources), each with its own users, usage data,
 * and apps. This is typically a school. Each Realm has its own xAPI and OneRoster URLs, and may
 * also have a RESPECT API server.
 *
 * @property xapi URL to xAPI endpoint
 * @property oneRoster URL to OneRoster endpoint
 * @property respectExt URL to Respect extensions endpoint (if available). Required for invites etc.
 */
@Serializable
class RespectRealm(
    val xapi: Url,
    val oneRoster: Url,
    val respectExt: Url?
)
