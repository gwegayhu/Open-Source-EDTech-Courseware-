package world.respect.app.appstate

import world.respect.domain.opds.model.LangMap
import world.respect.domain.opds.model.LangMapObjectValue
import world.respect.domain.opds.model.LangMapStringValue
import world.respect.domain.opds.model.ReadiumSubject
import world.respect.domain.opds.model.ReadiumSubjectObject
import world.respect.domain.opds.model.ReadiumSubjectStringValue


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
