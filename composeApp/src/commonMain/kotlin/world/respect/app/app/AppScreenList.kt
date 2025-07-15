//Transient properties are used as documented below, cannot be removed because they are needed for serialization
@file:Suppress("CanBeParameter")

package world.respect.app.app

import io.ktor.http.Url
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Mostly TypeSafe navigation for the RESPECT app. All serialized properties must be primitives or
 * strings (8/July/25: Compose multiplatform navigation does not like custom types when used with
 * toRoute).
 *
 * If using a non-primitive type (e.g. Url) then use a private constructor property with a primitive
 * type and then add a transient property
 */

@Serializable
sealed interface AppDestination

@Serializable
object Acknowledgement : AppDestination

@Serializable
object JoinClazzWithCode : AppDestination

@Serializable
object LoginScreen : AppDestination

@Serializable
object AppLauncher : AppDestination

@Serializable
object Assignment : AppDestination

@Serializable
object Clazz : AppDestination

@Serializable
object Report : AppDestination

@Serializable
object AppList : AppDestination

@Serializable
object EnterLink : AppDestination


/**
 * @property manifestUrl the URL to the RespectAppManifest for the given Respect compatible app
 */
@Serializable
class AppsDetail private constructor(
    private val manifestUrlStr: String
): AppDestination {

    @Transient
    val manifestUrl = Url(manifestUrlStr)

    companion object {

        fun create(manifestUrl: Url): AppsDetail {
            return AppsDetail(manifestUrl.toString())
        }

    }
}


/**
 * @property opdsFeedUrl the URL for an OPDS feed containing a list of learning units and/or links
 *           to other feeds
 */
@Serializable
class LearningUnitList(
    private val opdsFeedUrlStr: String
) : AppDestination {

    @Transient
    val opdsFeedUrl = Url(opdsFeedUrlStr)

    companion object {

        fun create(opdsFeedUrl: Url): LearningUnitList {
            return LearningUnitList(opdsFeedUrl.toString())
        }

    }

}


/**
 * @property learningUnitManifestUrl the URL of the OPDS Publication (Readium Manifest) for the
 *           learning unit as per RESPECT integration guide:
 *           https://github.com/UstadMobile/RESPECT-Consumer-App-Integration-Guide?tab=readme-ov-file#5-support-listing-and-launching-learning-units
 * @property refererUrl (optional), where available, the URL of the OPDS feed that referred the
 *           user to this learning unit. This allows the use of cached information from the feed
 *           to avoid waiting for the learningUnitManifestUrl to load to show the user the title,
 *           description, etc.
 * @property expectedIdentifier (optional), where a refererUrl is provided, to use cached feed
 *           metadata as above, the identifier of the publication within the feed.
 */
@Serializable
class LearningUnitDetail(
    private val learningUnitManifestUrlStr: String,
    private val refererUrlStr: String? = null,
    val expectedIdentifier: String? = null
) : AppDestination {

    @Transient
    val learningUnitManifestUrl = Url(learningUnitManifestUrlStr)

    @Transient
    val refererUrl = refererUrlStr?.let { Url(it) }

    companion object {

        fun create(
            learningUnitManifestUrl: Url,
            refererUrl: Url? = null,
            expectedIdentifier: String? = null
        ) = LearningUnitDetail(
            learningUnitManifestUrlStr = learningUnitManifestUrl.toString(),
            refererUrlStr = refererUrl?.toString(),
            expectedIdentifier = expectedIdentifier
        )

    }

}

