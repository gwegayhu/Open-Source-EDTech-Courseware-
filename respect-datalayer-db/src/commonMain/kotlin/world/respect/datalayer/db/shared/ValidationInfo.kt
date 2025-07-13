package world.respect.datalayer.db.shared

data class ValidationInfo(
    val lastModified: Long,
    val etag: String?,
)