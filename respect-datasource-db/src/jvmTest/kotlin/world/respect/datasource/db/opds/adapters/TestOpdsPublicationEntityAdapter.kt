package world.respect.datasource.db.opds.adapters

import kotlinx.serialization.json.Json
import world.respect.datasource.db.RespectDatabase
import world.respect.datasource.opds.model.OpdsPublication
import world.respect.lib.primarykeygen.PrimaryKeyGenerator
import world.respect.libxxhash.jvmimpl.XXStringHasherCommonJvm
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TestOpdsPublicationEntityAdapter {

    @Test
    fun givenConvertedToFromEntitiesWillBeEqual() {
        val json = Json {
            encodeDefaults = false
        }

        val publication = json.decodeFromString(
            OpdsPublication.serializer(),
            this::class.java.getResourceAsStream(
                "/world/respect/datasource/db/opds/adapters/lesson001.json"
            )!!.bufferedReader().use { it.readText() }
        )
        val pkGenerator = PrimaryKeyGenerator(RespectDatabase.TABLE_IDS)

        val entities = publication.asEntities(
            dataLoadResult = null,
            primaryKeyGenerator = pkGenerator,
            json = json,
            xxStringHasher = XXStringHasherCommonJvm(),
            opeOfeUid = 0,
            opeOfeIndex = 0,
        )

        val model = entities.asModel(
            json = json,
        )

        val modelData = model.data
        assertNotNull(modelData)
        assertEquals(publication.metadata.title, modelData.metadata.title)
        assertEquals(publication.links, modelData.links)
        assertEquals(publication.images, modelData.images)
        assertEquals(publication.resources, modelData.resources)
    }

}