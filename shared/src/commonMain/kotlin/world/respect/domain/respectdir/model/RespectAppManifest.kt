package world.respect.domain.respectdir.model

import com.eygraber.uri.Uri
import io.ktor.http.Url
import kotlinx.serialization.Serializable
import world.respect.domain.opds.model.LangMap
import world.respect.domain.opds.serialization.UriStringSerializer

/**
 * Represents the RESPECT manifest as described in the RESPECT Launcher App Integration Guide as
 * per https://github.com/UstadMobile/RESPECT-Consumer-App-Integration-Guide/
 */
@Serializable
data class RespectAppManifest(
    val name: LangMap,
    val license: String,
    val description: LangMap,
    val website: Url,
    val icon: Url? = null,
    @Serializable(with = UriStringSerializer::class)
    val learningUnits: Uri,
    @Serializable(with = UriStringSerializer::class)
    val defaultLaunchUri: Uri,
    val android: AndroidDetails? = null,
)

/**
 * Contains Android-specific details for the RESPECT manifest.
 */
@Serializable
data class AndroidDetails(
    val packageId: String,
    val stores: List<Url> = emptyList(),
)