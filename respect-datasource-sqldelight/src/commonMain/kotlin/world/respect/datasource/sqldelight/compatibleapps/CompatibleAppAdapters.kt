package world.respect.datasource.sqldelight.compatibleapps

import com.eygraber.uri.Uri
import io.ktor.http.Url
import kotlinx.serialization.json.Json
import world.respect.datasource.compatibleapps.model.RespectAppManifest
import world.respect.datasource.opds.model.LangMapObjectValue
import world.respect.datasource.opds.model.LangMapStringValue
import world.respect.datasource.sqldelight.CompatibleAppEntity

fun RespectAppManifest.asCompatibleAppEntity(
    json: Json,
): CompatibleAppEntity {
    return CompatibleAppEntity(
        caeUid = requireSelfUrl().toString().hashCode().toLong(),
        caeSelfUrl = requireSelfUrl().toString(),
        caeLicense = license,
        caeWebsite = website?.toString() ?: "",
        caeLearningUnits = learningUnits.toString(),
        caeAndroidPackageId = android?.packageId,
        caeAndroidStoreList = android?.stores?.map { it.toString() }?.let {
            json.encodeToString(it)
        },
        caeDefaultLaunchUri = defaultLaunchUri.toString(),
        caeAndroidSourceCode = android?.sourceCode?.toString(),
    )
}

fun CompatibleAppEntity.asRespectManifest(
    json: Json,
): RespectAppManifest {
    return RespectAppManifest(
        name = LangMapObjectValue(mapOf("name-${this.caeWebsite}" to "foo")),
        description = LangMapStringValue("here"),
        license = caeLicense,
        website = caeWebsite.takeIf { it.isNotBlank() }?.let { Url(it) },
        learningUnits = Uri.parse(caeLearningUnits),
        defaultLaunchUri = Uri.parse(caeDefaultLaunchUri),
        android = caeAndroidPackageId?.let { androidPackageId ->
            RespectAppManifest.AndroidDetails(
                packageId = androidPackageId,
                sourceCode = caeAndroidSourceCode?.let { Url(it) },
                stores = caeAndroidStoreList?.let { json.decodeFromString(it) } ?: emptyList()
            )
        },
        selfUrl = Url(caeSelfUrl),
    )
}
