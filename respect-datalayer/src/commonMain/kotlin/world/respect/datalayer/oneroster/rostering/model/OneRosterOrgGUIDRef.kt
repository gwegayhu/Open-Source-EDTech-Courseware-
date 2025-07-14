package world.respect.datalayer.oneroster.rostering.model

import com.eygraber.uri.Uri
import kotlinx.serialization.Serializable
import world.respect.datalayer.shared.serialization.UriStringSerializer

@Serializable
data class OneRosterOrgGUIDRef(
    val type: String = "org",
    @Serializable(with = UriStringSerializer::class)
    val href: Uri,
    val sourcedId: String,
)