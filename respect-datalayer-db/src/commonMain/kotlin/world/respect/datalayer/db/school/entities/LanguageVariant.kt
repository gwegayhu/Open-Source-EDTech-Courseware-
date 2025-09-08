package world.respect.datalayer.db.school.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Entity
@Serializable
class LanguageVariant() {


    @PrimaryKey(autoGenerate = true)
    var langVariantUid: Long = 0

    var langUid: Long = 0

    var countryCode: String? = null

    var name: String? = null

    var langVariantLocalChangeSeqNum: Long = 0

    var langVariantMasterChangeSeqNum: Long = 0

    var langVariantLastChangedBy: Int = 0

    var langVariantLct: Long = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        val that = other as LanguageVariant?

        if (langVariantUid != that!!.langVariantUid) return false
        if (langUid != that.langUid) return false
        if (if (countryCode != null) countryCode != that.countryCode else that.countryCode != null)
            return false
        return if (name != null) name == that.name else that.name == null
    }

    override fun hashCode(): Int {
        var result = (langVariantUid xor langVariantUid.ushr(32)).toInt()
        result = 31 * result + (langUid xor langUid.ushr(32)).toInt()
        result = 31 * result + if (countryCode != null) countryCode!!.hashCode() else 0
        result = 31 * result + if (name != null) name!!.hashCode() else 0
        return result
    }

    companion object {

        const val TABLE_ID = 10
    }
}
