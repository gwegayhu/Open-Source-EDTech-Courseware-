package world.respect.server.domain.realm.add

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import world.respect.datalayer.respect.model.RespectRealmDirectory
import world.respect.libxxhash.XXStringHasher
import kotlin.random.Random

/**
 * Used on the server side: Create a RealmDirectoryEntity RESPECT Directory that is managed on this
 * server.
 */
class AddServerManagedDirectoryCallback(
    private val xxStringHasher: XXStringHasher,
): RoomDatabase.Callback() {

    override fun onCreate(connection: SQLiteConnection) {
        val randomInvitePrefix = Random.nextInt(1, 10_000)
        val invitePrefixStr = randomInvitePrefix.toString().padStart(
            MAX_INVITE_PREFIX.toString().length, '0'
        )

        connection.execSQL("""
            INSERT INTO RealmDirectoryEntity(rdUid, rdUrl, rdInvitePrefix) 
            VALUES(
                ${xxStringHasher.hash(RespectRealmDirectory.SERVER_MANAGED_DIRECTORY_URL)},
                '${RespectRealmDirectory.SERVER_MANAGED_DIRECTORY_URL}',
                '$invitePrefixStr'
            )    
        """.trimIndent())
    }

    companion object {

        const val MAX_INVITE_PREFIX = 10_000
    }

}