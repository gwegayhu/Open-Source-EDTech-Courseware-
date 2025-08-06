package world.respect.datalayer.db.realmdirectory.ext

import world.respect.datalayer.respect.model.RespectRealm

val RespectRealm.virtualHostScopeId: String
    get() = self.toString()


