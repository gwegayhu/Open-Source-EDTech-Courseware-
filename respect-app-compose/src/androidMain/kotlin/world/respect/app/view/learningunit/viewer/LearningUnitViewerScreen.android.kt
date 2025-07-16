package world.respect.app.view.learningunit.viewer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import world.respect.app.components.webview.UstadAbstractWebViewClient
import world.respect.app.components.webview.UstadWebView
import world.respect.app.components.webview.UstadWebViewNavigatorAndroid
import world.respect.shared.viewmodel.learningunit.viewer.LearningUnitViewerViewModelUiState

@Composable
actual fun LearningUnitViewerScreen(uiState: LearningUnitViewerViewModelUiState) {

    val webViewNavigator = remember {
        UstadWebViewNavigatorAndroid(UstadAbstractWebViewClient())
    }

    UstadWebView(
        navigator = webViewNavigator,
        modifier = Modifier.fillMaxSize()
            .testTag("xapi_webview")
    )

    LaunchedEffect(uiState.url) {
        webViewNavigator.loadUrl(uiState.url.toString())
    }

}
