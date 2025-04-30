package world.respect.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.Flow
import moe.tlaster.precompose.navigation.Navigator
import world.respect.app.appstate.AppUiState
import world.respect.app.appstate.SnackBarDispatcher
import world.respect.app.appstate.nav.NavCommand
import world.respect.app.viewmodel.AppLauncherScreenViewModel

@Composable
fun AppNavHost(
    navigator: Navigator,
    onSetAppUiState: (AppUiState) -> Unit,
    onShowSnackBar: SnackBarDispatcher,
    persistNavState: Boolean = false,
    modifier: Modifier,
    navCommandFlow: Flow<NavCommand>? = null,
    initialRoute: String = "/${AppLauncherScreenViewModel.DEST_NAME}",
) {

}