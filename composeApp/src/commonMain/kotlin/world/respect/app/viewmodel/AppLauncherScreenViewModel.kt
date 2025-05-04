package world.respect.app.viewmodel

import moe.tlaster.precompose.viewmodel.ViewModel

class AppLauncherScreenViewModel : ViewModel() {
    companion object {
        const val DEST_NAME = "AppLauncherScreen"
    }

    val title: String = "Welcome to App Launcher"
}