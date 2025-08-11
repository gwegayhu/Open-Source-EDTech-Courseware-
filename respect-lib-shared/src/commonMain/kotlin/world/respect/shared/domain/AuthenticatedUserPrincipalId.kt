package world.respect.shared.domain

/**
 * Represents an authenticated user principal : the server has checked the authorization header
 * and determined the user is authenticated.
 */
data class AuthenticatedUserPrincipalId(
    val guid: String,
)