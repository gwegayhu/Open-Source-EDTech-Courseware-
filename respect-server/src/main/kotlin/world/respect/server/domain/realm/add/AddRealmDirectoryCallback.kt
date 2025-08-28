package world.respect.server.domain.realm.add

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import jdk.jfr.internal.SecuritySupport.getResourceAsStream
import world.respect.libxxhash.XXStringHasher
import java.util.Properties

class AddRealmDirectoryCallback(
    private val xxStringHasher: XXStringHasher,
) : RoomDatabase.Callback() {

    override fun onCreate(connection: SQLiteConnection) {

        val props = Properties()

        val defaultStream = getResourceAsStream("respect-directories/default.properties")
            ?: throw IllegalStateException("default.properties not found in resources")
        props.load(defaultStream)

        val localStream = getResourceAsStream("respect-directories/local.properties")
        if (localStream != null) {
            props.load(localStream)
        }
        props.forEach { key, value ->
            val url = value.toString()
            val uid = xxStringHasher.hash(url)
            val prefix = key.toString()

            connection.execSQL("""
            INSERT INTO RealmDirectoryEntity(rdUid, rdUrl, rdInvitePrefix) 
            VALUES($uid,'$url','$prefix')    
            """.trimIndent())
        }
    }

}
