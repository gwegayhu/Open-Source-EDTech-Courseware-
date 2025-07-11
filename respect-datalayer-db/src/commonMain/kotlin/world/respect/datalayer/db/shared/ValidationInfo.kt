package world.respect.datalayer.db.shared

import world.respect.datalayer.networkvalidation.NetworkValidationInfo

data class ValidationInfo(
    val lastModified: Long,
    val etag: String?,
) {

    fun asNetworkValidationInfo(): NetworkValidationInfo {
        return NetworkValidationInfo(
            lastModified = lastModified,
            etag = etag,
        )
    }

}