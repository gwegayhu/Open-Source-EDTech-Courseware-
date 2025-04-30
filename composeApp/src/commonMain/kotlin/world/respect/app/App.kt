package world.respect.app


import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import kotlinx.coroutines.flow.Flow
import moe.tlaster.precompose.navigation.NavOptions
import kotlin.Boolean
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.rememberNavigator
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExtendedFloatingActionButton
import world.respect.app.appstate.AppUiState
import world.respect.app.appstate.FabUiState
import world.respect.app.appstate.SnackBarDispatcher
import world.respect.app.appstate.nav.NavCommand
import world.respect.app.viewmodel.AppLauncherScreenViewModel


data class TopNavigationItem(
    val destRoute: String,
    val icon: ImageVector,
    val label: String
)

val APP_TOP_LEVEL_NAV_ITEMS = listOf(
    TopNavigationItem(
        destRoute = "AppLauncherScreen",
        icon = Icons.Filled.Home,
        label = "Apps"
    ),
    TopNavigationItem(
        destRoute = "AssignmentsScreen",
        icon = Icons.Filled.Menu,
        label = "Assignments"
    ),
    TopNavigationItem(
        destRoute = "ClassesScreen",
        icon = Icons.Filled.MailOutline,
        label = "Classes"
    ),
    TopNavigationItem(
        destRoute = "ReportScreen",
        icon = Icons.Filled.Person,
        label = "Report"
    )
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun App(
    widthClass: SizeClass = SizeClass.MEDIUM,
    persistNavState: Boolean = false,
    useBottomBar: Boolean = true,
    navigator: Navigator = rememberNavigator(),
    onAppStateChanged: (AppUiState) -> Unit = { },
    navCommandFlow: Flow<NavCommand>? = null,
    initialRoute: String = "/${AppLauncherScreenViewModel.DEST_NAME}",
    ) {
    val appUiState = remember {
        mutableStateOf(
            AppUiState(
                navigationVisible = false,
                hideAppBar = true,
            )
        )
    }
    val currentLocation by navigator.currentEntry.collectAsState(null)
    var appUiStateVal by appUiState
    LaunchedEffect(appUiStateVal) {
        onAppStateChanged(appUiStateVal)
    }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val onShowSnackBar: SnackBarDispatcher = remember {
        SnackBarDispatcher {  snack ->
            scope.launch {
                snackbarHostState.showSnackbar(snack.message, snack.action)
            }
        }
    }
    CompositionLocalProvider(LocalWidthClass provides widthClass) {
        Scaffold(
            topBar = {
                if(!appUiStateVal.hideAppBar) {
                    RespectAppBar(
                        compactHeader = (widthClass != SizeClass.EXPANDED),
                        appUiState = appUiStateVal,
                        navigator = navigator,
                        currentLocation = currentLocation,
                    )
                }
            },
            bottomBar = {
                //As per https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#navigationbar
                var selectedTopLevelItemIndex by remember { mutableIntStateOf(0) }
                if(useBottomBar) {
                    /**
                     * Set the selected item. Relying on onClick misses when the user switches accounts
                     * and goes back to the start screen (courses).
                     */
                    LaunchedEffect(currentLocation?.path) {
                        val pathVal = currentLocation?.path ?: return@LaunchedEffect
                        val topLevelIndex = APP_TOP_LEVEL_NAV_ITEMS.indexOfFirst {
                            "/${it.destRoute}" == pathVal
                        }

                        if(topLevelIndex >= 0)
                            selectedTopLevelItemIndex = topLevelIndex
                    }

                    if(appUiStateVal.navigationVisible && !appUiStateVal.hideBottomNavigation) {
                        NavigationBar {
                            APP_TOP_LEVEL_NAV_ITEMS.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    icon = {
                                        Icon(item.icon, contentDescription = null)
                                    },
                                  //  label = { Text(stringResource(item.label)) },
                                    selected = selectedTopLevelItemIndex == index,
                                    onClick = {
                                        navigator.navigate(
                                            route  = "/${item.destRoute}",
                                            options = NavOptions(popUpTo = PopUpTo.First(inclusive = true))
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            },
            floatingActionButton = {
                if(appUiStateVal.fabState.visible) {
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
                            val imageVector = when(appUiStateVal.fabState.icon)  {
                                FabUiState.FabIcon.ADD -> Icons.Default.Menu
                                FabUiState.FabIcon.EDIT -> Icons.Default.Menu
                                else -> null
                            }
                            if(imageVector != null) {
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
            AppNavHost(
                navigator = navigator,
                onSetAppUiState = {
                    appUiStateVal = it
                },
                modifier = Modifier
                    .padding(innerPadding)
                    /*
                     * consumeWindowInsets is required so that subsequent use of imePadding doesn't result
                     * in extra space when the soft keyboard is open e.g. count the padding from the
                     * spacing against the padding required for the keyboard (otherwise both get added
                     * together).
                     */
                    .consumeWindowInsets(innerPadding)
                    .imePadding(),
                persistNavState = persistNavState,
                onShowSnackBar = onShowSnackBar,
                navCommandFlow = navCommandFlow,
                initialRoute = initialRoute,
            )
        }
    }

}
