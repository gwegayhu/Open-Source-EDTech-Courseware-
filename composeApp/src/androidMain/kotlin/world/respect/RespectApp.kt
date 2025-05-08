package world.respect


import android.app.Application
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import world.respect.app.viewmodel.ReportScreenViewModel

class RespectApp : Application(), DIAware {
    override val di = DI.lazy {
        // Initialize your DI container and bind the dependencies
    }
}
