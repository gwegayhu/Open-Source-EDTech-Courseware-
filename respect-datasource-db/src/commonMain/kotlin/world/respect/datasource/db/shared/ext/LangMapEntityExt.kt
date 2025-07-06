package world.respect.datasource.db.shared.ext

import world.respect.datasource.db.shared.entities.LangMapEntity

val LangMapEntity.langMapKey: String
    get() = if(lmeRegion != null) {
        "$lmeLang-$lmeRegion"
    }else {
        lmeLang
    }