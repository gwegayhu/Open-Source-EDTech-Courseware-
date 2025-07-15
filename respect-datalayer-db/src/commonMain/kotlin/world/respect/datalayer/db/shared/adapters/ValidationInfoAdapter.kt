package world.respect.datalayer.db.shared.adapters

import world.respect.datalayer.db.shared.ValidationInfo
import world.respect.datalayer.networkvalidation.NetworkValidationInfo

fun ValidationInfo.asNetworkValidationInfo(): NetworkValidationInfo {
    return NetworkValidationInfo(
        lastModified = lastModified,
        etag = etag,
    )
}