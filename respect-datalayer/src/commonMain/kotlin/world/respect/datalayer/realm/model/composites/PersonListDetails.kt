package world.respect.datalayer.realm.model.composites


/**
 * Data class for the details shown in the list (fewer fields than full entity; exists for performance
 * reasons)
 */
data class PersonListDetails(
    val guid: String,
    val givenName: String,
    val familyName: String,
    val userName: String?
)
