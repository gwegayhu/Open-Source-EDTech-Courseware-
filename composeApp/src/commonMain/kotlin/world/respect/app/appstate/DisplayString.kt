package world.respect.app.appstate

import world.respect.datalayer.opds.model.LangMap
import world.respect.datalayer.opds.model.LangMapObjectValue
import world.respect.datalayer.opds.model.LangMapStringValue
import world.respect.datalayer.opds.model.ReadiumSubject
import world.respect.datalayer.opds.model.ReadiumSubjectObject
import world.respect.datalayer.opds.model.ReadiumSubjectStringValue


fun ReadiumSubject.toDisplayString(preferredLanguages: List<String> = listOf("en")): String = when (this) {
    is ReadiumSubjectStringValue -> this.value
    is ReadiumSubjectObject -> {
        this.name.getTitle(preferredLanguages)
    }
}

fun LangMap.getTitle(preferredLanguages: List<String> = listOf("en")): String {
    return when (this) {
        is LangMapStringValue -> this.value
        is LangMapObjectValue -> {
            preferredLanguages.firstNotNullOfOrNull { this.map[it] }
                ?: this.map.values.firstOrNull()
                ?: "Untitled"
        }
    }
}
