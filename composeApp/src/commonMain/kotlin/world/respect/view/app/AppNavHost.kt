package world.respect.view.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.Flow
import moe.tlaster.precompose.navigation.Navigator
import world.respect.AppLauncherScreenViewModel
import world.respect.impl.appstate.AppUiState
import world.respect.impl.appstate.SnackBarDispatcher
import world.respect.impl.appstate.nav.NavCommand

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