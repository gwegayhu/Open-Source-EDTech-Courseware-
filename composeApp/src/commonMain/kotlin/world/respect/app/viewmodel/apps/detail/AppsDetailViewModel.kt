package world.respect.app.viewmodel.apps.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import respect.composeapp.generated.resources.Res
import respect.composeapp.generated.resources.apps_detail
import world.respect.DummyRepo
import world.respect.app.app.AppsDetail
import world.respect.app.model.applist.FakeAppDataSource
import world.respect.app.model.lesson.FakeOpdsDataSource
import world.respect.app.viewmodel.RespectViewModel
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataLoadResult
import world.respect.datasource.compatibleapps.model.RespectAppManifest
import world.respect.datasource.opds.model.OpdsPublication


data class AppsDetailUiState(
    val appDetail: RespectAppManifest? = null,
    val publications: List<OpdsPublication> = emptyList(),
)

class AppsDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val dummyRepo: DummyRepo,
) : RespectViewModel(savedStateHandle) {

    private val appDataSource: FakeAppDataSource = FakeAppDataSource()

    private val opdsDataSource: FakeOpdsDataSource = FakeOpdsDataSource()

    private val _uiState = MutableStateFlow(AppsDetailUiState())

    val uiState = _uiState.asStateFlow()

    private val route: AppsDetail = savedStateHandle.toRoute()

    init {
        //Get the argument here
        println(route.manifestUrl)
        println(dummyRepo)

        viewModelScope.launch {
            _appUiState.update {
                it.copy(title = getString(resource = Res.string.apps_detail))
            }
            //once navigation is fixed will pass argument learning units
            appDataSource.getApp(
                manifestUrl = "",
                loadParams = DataLoadParams()
            ).collect { result ->
                when (result) {
                    is DataLoadResult -> {
                        val appDetail = result.data
                        _uiState.update {
                            it.copy(
                                appDetail = appDetail
                            )
                        }
                    }

                    else -> {}
                }
            }
            opdsDataSource.loadOpdsFeed(
                url = "",
                params = DataLoadParams()
            ).collect { result ->
                when (result) {
                    is DataLoadResult -> {
                        _uiState.update {
                            it.copy(
                                publications = result.data?.publications ?: emptyList(),
                            )
                        }
                    }

                    else -> {}
                }
            }

        }
    }
}
