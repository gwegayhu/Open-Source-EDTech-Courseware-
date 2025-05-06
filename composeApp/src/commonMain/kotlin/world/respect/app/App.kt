package world.respect.app

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import kotlin.Boolean
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ImportContacts
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.navigation.compose.rememberNavController
import world.respect.app.appstate.AppUiState
import world.respect.app.appstate.FabUiState
import world.respect.app.appstate.SnackBarDispatcher

data class TopNavigationItem(
    val destRoute: Any,
    val icon: ImageVector,
    val label: String
)

val APP_TOP_LEVEL_NAV_ITEMS = listOf(
    TopNavigationItem(
        destRoute = AppLauncher,
        icon = Icons.Filled.GridView,
        label = "Apps"
    ),
    TopNavigationItem(
        destRoute = Assignment,
        icon = Icons.Filled.ImportContacts,
        label = "Assignments"
    ),
    TopNavigationItem(
        destRoute = Clazz,
        icon = Icons.Filled.LibraryBooks,
        label = "Clazz"
    ),
    TopNavigationItem(
        destRoute = Report,
        icon = Icons.Filled.BarChart,
        label = "Report"
    )
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun App(
    widthClass: SizeClass = SizeClass.MEDIUM,
    useBottomBar: Boolean = true,
    onAppStateChanged: (AppUiState) -> Unit = { }) {
    val appUiState = remember {
        mutableStateOf(
            AppUiState(
                navigationVisible = true,
                hideAppBar = false,
            )
        )
    }

    val navController = rememberNavController()
    var appUiStateVal by appUiState
    LaunchedEffect(appUiStateVal) {
        onAppStateChanged(appUiStateVal)
    }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val onShowSnackBar: SnackBarDispatcher = remember {
        SnackBarDispatcher { snack ->
            scope.launch {
                snackbarHostState.showSnackbar(snack.message, snack.action)
            }
        }
    }
    CompositionLocalProvider(LocalWidthClass provides widthClass) {
        Scaffold(
            topBar = {
                if (!appUiStateVal.hideAppBar) {
                    RespectAppBar(
                        compactHeader = (widthClass != SizeClass.EXPANDED),
                        appUiState = appUiStateVal,
                        navController = navController,
                    )
                }
            },
            bottomBar = {
                var selectedTopLevelItemIndex by remember { mutableIntStateOf(0) }
                if (useBottomBar) {
                    if (appUiStateVal.navigationVisible && !appUiStateVal.hideBottomNavigation) {
                        NavigationBar {
                            APP_TOP_LEVEL_NAV_ITEMS.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    icon = {
                                        Icon(item.icon, contentDescription = null)
                                    },
                                   label = { Text(item.label) },
                                    selected = selectedTopLevelItemIndex == index,
                                    onClick = {
                                        navController.navigate(item.destRoute)
                                        selectedTopLevelItemIndex = index
                                    }
                                )
                            }
                        }
                    }
                }
            },
            floatingActionButton = {
                if (appUiStateVal.fabState.visible) {
                    ExtendedFloatingActionButton(
                        modifier = Modifier.testTag("floating_action_button"),
                        onClick = appUiStateVal.fabState.onClick,
                        text = {
                            Text(
                                modifier = Modifier.testTag("floating_action_button_text"),
                                text = appUiStateVal.fabState.text ?: ""
                            )
                        },
                        icon = {
                            val imageVector = when (appUiStateVal.fabState.icon) {
                                FabUiState.FabIcon.ADD -> Icons.Default.Menu
                                FabUiState.FabIcon.EDIT -> Icons.Default.Menu
                                else -> null
                            }
                            if (imageVector != null) {
                                Icon(
                                    imageVector = imageVector,
                                    contentDescription = null,
                                )
                            }
                        }
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            },
        ) { innerPadding ->
            AppNavHost(navController = navController)
        }
    }

}
