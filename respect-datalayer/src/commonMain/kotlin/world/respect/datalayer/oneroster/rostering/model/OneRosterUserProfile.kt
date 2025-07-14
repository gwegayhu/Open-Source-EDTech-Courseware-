package world.respect.datalayer.oneroster.rostering.model

import com.eygraber.uri.Uri
import kotlinx.serialization.Serializable

@Serializable
data class OneRosterUserProfile(
    val profileId: Uri,
    val profileType: String,
    val vendorId: String,
    val applicationId: String? = null,
    val description: String? = null,
)