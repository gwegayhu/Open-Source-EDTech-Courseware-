package world.respect

import android.app.Application
import org.koin.core.context.startKoin

class RespectApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appKoinModule)
        }
    }
}
