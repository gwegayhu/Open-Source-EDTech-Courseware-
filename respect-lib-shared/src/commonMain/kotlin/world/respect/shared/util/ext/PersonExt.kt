package world.respect.shared.util.ext

import world.respect.datalayer.realm.model.Person

fun Person.fullName(): String = buildString {
    append(givenName)
    append(" ")
    middleName?.also {
        append(it)
        append(" ")
    }
    append(familyName)
}
