package world.respect.app.viewmodel

import androidx.lifecycle.ViewModel


class AppLauncherScreenViewModel : ViewModel() {
    companion object {
        const val DEST_NAME = "AppLauncherScreen"
    }

    val title: String = "Welcome to App Launcher"
}
