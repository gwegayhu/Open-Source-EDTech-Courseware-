package world.respect.app.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.map
import world.respect.app.appstate.AppUiState
import world.respect.app.effects.AppUiStateEffect
import world.respect.navigation.NavCommandEffect
import world.respect.navigation.RespectComposeNavController
import kotlin.reflect.KClass

@Composable
fun <T : RespectViewModel> respectViewModel(
    modelClass: KClass<T>,
    appUiStateMap: ((AppUiState) -> AppUiState)? = null,
    onSetAppUiState: (AppUiState) -> Unit,
    navController: RespectComposeNavController,
): T {

    val viewModel = viewModel(modelClass)

    val uiStateFlow = remember(viewModel.appUiState, appUiStateMap) {
        if(appUiStateMap != null) {
            viewModel.appUiState.map(appUiStateMap)
        }else {
            viewModel.appUiState
        }
    }

    AppUiStateEffect(
        appUiStateFlow = uiStateFlow,
        onSetAppUiState = onSetAppUiState,
    )

    NavCommandEffect(
        navHostController = navController,
        navCommandFlow = viewModel.navCommandFlow
    )

    return viewModel
}

