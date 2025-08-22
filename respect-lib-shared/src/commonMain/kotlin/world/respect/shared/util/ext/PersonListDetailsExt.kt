package world.respect.shared.util.ext

import world.respect.datalayer.realm.model.composites.PersonListDetails

fun PersonListDetails.fullName() = "$givenName $familyName"
