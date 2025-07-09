package world.respect.datasource.db.compatibleapps.adapters

import com.eygraber.uri.Uri
import io.ktor.http.Url
import kotlinx.serialization.json.Json
import world.respect.datasource.DataLoadMetaInfo
import world.respect.datasource.DataLoadResult
import world.respect.datasource.LoadingStatus
import world.respect.datasource.compatibleapps.model.RespectAppManifest
import world.respect.datasource.db.shared.adapters.asEntities
import world.respect.datasource.db.shared.adapters.toModel
import world.respect.datasource.db.compatibleapps.entities.CompatibleAppEntity
import world.respect.datasource.db.shared.entities.LangMapEntity
import world.respect.libxxhash.XXStringHasher
import world.respect.datasource.db.shared.entities.LangMapEntity.PropType.RESPECT_MANIFEST_NAME
import world.respect.datasource.db.shared.entities.LangMapEntity.PropType.RESPECT_MANIFEST_DESCRIPTION

/**
 * All the entities required to represent a RespectAppManifest.
 */
data class CompatibleAppEntities(
    val compatibleAppEntity: CompatibleAppEntity,
    val langMapEntities: List<LangMapEntity>,
)

fun DataLoadResult<RespectAppManifest>.asEntities(
    json: Json,
    xxStringHasher: XXStringHasher,
) : CompatibleAppEntities? {
    val manifest = data ?: return null
    val caeUid = xxStringHasher.hash(metaInfo.requireUrl().toString())
    return CompatibleAppEntities(
        compatibleAppEntity = CompatibleAppEntity(
            caeUid = caeUid,
            caeUrl = metaInfo.requireUrl().toString(),
            caeLastModified = metaInfo.lastModified,
            caeEtag = metaInfo.etag,
            caeLicense = manifest.license,
            caeWebsite = manifest.website?.toString() ?: "",
            caeLearningUnits = manifest.learningUnits.toString(),
            caeAndroidPackageId = manifest.android?.packageId,
            caeAndroidStoreList = manifest.android?.stores?.map { it.toString() }?.let {
                json.encodeToString(it)
            },
            caeDefaultLaunchUri = manifest.defaultLaunchUri.toString(),
            caeAndroidSourceCode = manifest.android?.sourceCode?.toString(),
        ),
        langMapEntities = manifest.name.asEntities(
            lmeTopParentType = LangMapEntity.TopParentType.RESPECT_MANIFEST,
            lmeTopParentUid1 = caeUid,
            lmePropType= RESPECT_MANIFEST_NAME,
        ) + (manifest.description?.asEntities(
            lmeTopParentType = LangMapEntity.TopParentType.RESPECT_MANIFEST,
            lmeTopParentUid1 = caeUid,
            lmePropType = RESPECT_MANIFEST_DESCRIPTION,
        ) ?: emptyList()),
    )
}

fun CompatibleAppEntity.asModel(
    langMapEntities: List<LangMapEntity>,
    json: Json,
): DataLoadResult<RespectAppManifest>? {
    return CompatibleAppEntities(
        compatibleAppEntity = this,
        langMapEntities = langMapEntities,
    ).asModel(
        json = json
    )
}

fun CompatibleAppEntities.asModel(
    json: Json,
): DataLoadResult<RespectAppManifest> {
    return DataLoadResult(
        data = RespectAppManifest(
            name = langMapEntities.filter { it.lmePropType == RESPECT_MANIFEST_NAME }.toModel(),
            description = langMapEntities.filter { it.lmePropType == RESPECT_MANIFEST_DESCRIPTION }.toModel(),
            license = compatibleAppEntity.caeLicense,
            website = compatibleAppEntity.caeWebsite.takeIf { it.isNotBlank() }?.let { Url(it) },
            learningUnits = Uri.parse(compatibleAppEntity.caeLearningUnits),
            defaultLaunchUri = Uri.parse(compatibleAppEntity.caeDefaultLaunchUri),
            android = compatibleAppEntity.caeAndroidPackageId?.let { androidPackageId ->
                RespectAppManifest.AndroidDetails(
                    packageId = androidPackageId,
                    sourceCode = compatibleAppEntity.caeAndroidSourceCode?.let { Url(it) },
                    stores = compatibleAppEntity.caeAndroidStoreList?.let {
                        json.decodeFromString(it)
                    } ?: emptyList()
                )
            },
        ),
        metaInfo = DataLoadMetaInfo(
            status = LoadingStatus.LOADED,
            lastModified = compatibleAppEntity.caeLastModified,
            etag = compatibleAppEntity.caeEtag,
            url = Url(compatibleAppEntity.caeUrl),
        )
    )
}
