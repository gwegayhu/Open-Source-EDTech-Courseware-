package world.respect.shared.domain.realm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class MakeRealmPathDirUseCaseAndroid(
    private val realmPath: RespectRealmPath
): MakeRealmPathDirUseCase {

    override suspend fun invoke() {
        withContext(Dispatchers.IO) {
            File(realmPath.path.toString()).takeIf { !it.exists() }?.mkdirs()
        }
    }
}