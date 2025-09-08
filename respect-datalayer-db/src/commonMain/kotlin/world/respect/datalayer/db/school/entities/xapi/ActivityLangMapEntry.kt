package world.respect.datalayer.db.school.entities.xapi

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    primaryKeys = ["almeActivityUid", "almeHash"]
)
/**
 * Entity used to store the string values for the langmap of Activity's name or display properties,
 * and the string values for the langmap associated with any of the interaction properties.
 *
 * @param almeActivityUid the activity uid that this lang map is related to
 * @param almeHash hash of "propertyname-langcode" where  propertyname is PROPNAME_NAME or
 * PROPNAME_DESCRIPTION as per PROP_NAME_constants and langcode is almeLangCode OR
 * an interaction property name (choices,scale,source,target,steps) - id - lang code e.g.
 * "choices-choiceid-en-US"
 * @param almeLangCode the lang code as per the xAPI language map eg en-US
 * @param almePropName the property name as per PROP_NAME_constants (description or name), OR, for
 * an interaction property name (choices,scale,source,target,steps), e.g. "choices-choiceid"
 * @param almeValue the string value for the given language
 * @param almeAieHash where this entity represents a langmap for an interaction property, the hash
 * as per ActivityInteractionEntity.aieHash
 */
data class ActivityLangMapEntry(
    var almeActivityUid: Long = 0,

    var almeHash: Long = 0,

    var almeLangCode: String? = null,

    var almePropName: String? = null,

    var almeValue: String? = null,

    var almeAieHash: Long = 0,

    var almeLastMod: Long = 0,
) {
    companion object {
        const val TABLE_ID = 6442
        const val PROPNAME_NAME = "name"
        const val PROPNAME_DESCRIPTION = "description"
    }
}

