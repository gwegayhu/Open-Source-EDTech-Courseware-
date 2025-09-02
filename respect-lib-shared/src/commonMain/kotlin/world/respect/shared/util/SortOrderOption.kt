package world.respect.shared.util

import org.jetbrains.compose.resources.StringResource


data class SortOrderOption(
    /**
     * MR.strings.constant for the field to be sorted by e.g. MR.strings.firstNames
     */
    val fieldMessageId: StringResource,

    /**
     * The flag value that is used by the DAO for this sort option e.g. PersonDao.SORT_FIRST_NAME_ASC
     */
    val flag: Int,

    /**
     * The order
     *
     * Ascending = true, descending = false, not applicable = null
     * (e.g. 'Most recent' which is implicitly descending)
     */
    val order: Boolean?
)