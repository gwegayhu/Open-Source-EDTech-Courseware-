package world.respect.app.viewmodel

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import world.respect.shared.viewmodel.RespectViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import world.respect.app.app.EditStrand
import world.respect.shared.navigation.NavCommand
import world.respect.shared.viewmodel.app.appstate.AppUiState

data class CurriculumDetailUiState(
    val curriculumId: String = "",
    val curriculumName: String = "",
    val strands: List<CurriculumStrand> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class CurriculumStrand(
    val id: String,
    val name: String,
    val description: String = "",
    val isActive: Boolean = true
)

class CurriculumDetailViewModel(
    savedStateHandle: SavedStateHandle
) : RespectViewModel(savedStateHandle) {

    private var curriculumId: String = ""
    private var curriculumName: String = ""

    private val _uiState = MutableStateFlow(CurriculumDetailUiState())
    val uiState: StateFlow<CurriculumDetailUiState> = _uiState.asStateFlow()

    // These are required by RespectViewModel
    override val _navCommandFlow = MutableSharedFlow<NavCommand>()
    override val navCommandFlow = _navCommandFlow.asSharedFlow()

    override val appUiState: StateFlow<AppUiState> = MutableStateFlow(
        AppUiState(
            hideAppBar = true,
            navigationVisible = true
        )
    ).asStateFlow()

    fun setCurriculumData(curriculumId: String, curriculumName: String) {
        this.curriculumId = curriculumId
        this.curriculumName = curriculumName
        _uiState.value = _uiState.value.copy(
            curriculumId = curriculumId,
            curriculumName = curriculumName
        )
        loadCurriculumDetails()
    }

    fun onBackClick() {
        viewModelScope.launch {
            _navCommandFlow.emit(NavCommand.Navigate("back"))
        }
    }

    fun onAddStrandClick() {
        viewModelScope.launch {
            _navCommandFlow.emit(
                NavCommand.Navigate(
                    EditStrand(
                        curriculumId = curriculumId,
                        strandId = null
                    )
                )
            )
        }
    }

    fun onStrandClick(strand: CurriculumStrand) {
        viewModelScope.launch {
            _navCommandFlow.emit(
                NavCommand.Navigate(
                    EditStrand(
                        curriculumId = curriculumId,
                        strandId = strand.id
                    )
                )
            )
        }
    }

    fun onRefresh() {
        loadCurriculumDetails()
    }

    private fun loadCurriculumDetails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {

                val strands = emptyList<CurriculumStrand>()

                _uiState.value = _uiState.value.copy(
                    strands = strands,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}