package world.respect.app.viewmodel.apps.detail

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import world.respect.app.model.appsdetail.AppsDetailModel
import world.respect.app.viewmodel.RespectViewModel
import respect.composeapp.generated.resources.Res
import respect.composeapp.generated.resources.apps_detail
import world.respect.app.model.applist.FakeAppDataSource
import world.respect.app.model.appsdetail.Images
import world.respect.app.model.lesson.FakeOpdsDataSource
import world.respect.datasource.DataLoadParams
import world.respect.datasource.DataLoadResult
import world.respect.datasource.compatibleapps.model.RespectAppManifest
import world.respect.datasource.opds.model.LangMapStringValue
import world.respect.datasource.opds.model.OpdsPublication
import world.respect.datasource.opds.model.ReadiumContributorStringValue
import world.respect.datasource.opds.model.ReadiumLink
import world.respect.datasource.opds.model.ReadiumMetadata
import world.respect.datasource.opds.model.ReadiumSubjectStringValue


data class AppsDetailUiState(
    val appDetail: RespectAppManifest? = null,
    val publications: List<OpdsPublication> = emptyList(),
)

class AppsDetailViewModel(
    private val appDataSource: FakeAppDataSource = FakeAppDataSource(),
    private val opdsDataSource: FakeOpdsDataSource = FakeOpdsDataSource()
) : RespectViewModel() {
    private val _uiState = MutableStateFlow(AppsDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
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
