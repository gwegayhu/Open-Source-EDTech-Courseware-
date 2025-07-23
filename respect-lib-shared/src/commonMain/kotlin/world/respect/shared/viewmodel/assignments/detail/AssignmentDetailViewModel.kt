package world.respect.shared.viewmodel.assignments.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.ktor.http.Url
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import world.respect.datalayer.DataLoadParams
import world.respect.datalayer.DataLoadState
import world.respect.datalayer.DataLoadingState
import world.respect.datalayer.compatibleapps.model.RespectAppManifest
import world.respect.datalayer.dclazz.DClazzDataSource
import world.respect.datalayer.dclazz.model.DAssignment
import world.respect.datalayer.ext.dataOrNull
import world.respect.datalayer.opds.model.OpdsPublication
import world.respect.shared.datasource.RespectAppDataSourceProvider
import world.respect.shared.domain.account.RespectAccountManager
import world.respect.shared.domain.launchapp.LaunchAppUseCase
import world.respect.shared.navigation.AssignmentDetail
import world.respect.shared.util.isActiveUserTeacher
import world.respect.shared.viewmodel.RespectViewModel

data class AssignmentDetailUiState(
    val assignment: DAssignment? = null,
    val isTeacher: Boolean = false,
    val app: DataLoadState<RespectAppManifest> = DataLoadingState(),
    val learningUnit: DataLoadState<OpdsPublication> = DataLoadingState(),
)

class AssignmentDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val dClazzDataSource: DClazzDataSource,
    private val accountManager: RespectAccountManager,
    private val launchAppUseCase: LaunchAppUseCase,
    private val dataSourceProvider: RespectAppDataSourceProvider,
): RespectViewModel(savedStateHandle) {

    private val _uiState = MutableStateFlow(AssignmentDetailUiState())

    val uiState = _uiState.asStateFlow()

    private val route: AssignmentDetail = savedStateHandle.toRoute()

    init {
        _uiState.update {
            it.copy(isTeacher = accountManager.isActiveUserTeacher())
        }

        viewModelScope.launch {
            dClazzDataSource.getAssignmentAsFlow(
                route.assignmentId
            ).collectLatest { assignment ->
                println("assignment = $assignment")
                _uiState.update { prev ->
                    prev.copy(assignment = assignment)
                }

                _appUiState.update { prev ->
                    prev.copy(
                        title = assignment?.title ?: "",
                    )
                }

                if(assignment == null) return@collectLatest

                launch {
                    dataSourceProvider.getDataSource(
                        activeAccount
                    ).compatibleAppsDataSource.getAppAsFlow(
                        Url(assignment.appManifestUrl, ), DataLoadParams()
                    ).collect { app ->
                        _uiState.update { prev ->
                            prev.copy(
                                app = app
                            )
                        }
                    }
                }

                launch {
                    dataSourceProvider.getDataSource(
                        activeAccount
                    ).opdsDataSource.loadOpdsPublication(
                        url = Url(assignment.learningUnitManifestUrl),
                        DataLoadParams(),
                        referrerUrl = null,
                        expectedPublicationId = null,
                    ).collect { publication ->
                        _uiState.update { prev ->
                            prev.copy(
                                learningUnit = publication
                            )
                        }
                    }
                }
            }
        }
    }

    fun onClickOpen() {
        val appManifest = _uiState.value.app.dataOrNull() ?: return
        val learningUnitId = _uiState.value.assignment?.learningUnitId ?: return

        viewModelScope.launch {
            launchAppUseCase(
                app = appManifest,
                account = activeAccount,
                learningUnitId = Url(learningUnitId),
                navigateFn = {
                    _navCommandFlow.tryEmit(it)
                }
            )
        }
    }

}