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
import world.respect.shared.navigation.NavCommand
import world.respect.shared.viewmodel.app.appstate.AppUiState

class StrandEditViewModel(
    savedStateHandle: SavedStateHandle
) : RespectViewModel(savedStateHandle) {

    private var curriculumId: String = ""
    private var strandId: String? = null

    data class StrandEditUiState(
        val strand: Strand? = null,
        val name: String = "",
        val learningObjectives: String = "",
        val outcomes: String = "",
        val isLoading: Boolean = false,
        val isEditMode: Boolean = false,
        val nameError: String? = null,
        val learningObjectivesError: String? = null,
        val outcomesError: String? = null,
        val error: String? = null
    ) {
        val isValid: Boolean
            get() = name.isNotBlank() && learningObjectives.isNotBlank() &&
                    outcomes.isNotBlank() && nameError == null &&
                    learningObjectivesError == null && outcomesError == null
    }

    data class Strand(
        val id: String,
        val name: String,
        val learningObjectives: String,
        val outcomes: String,
        val isActive: Boolean
    )

    private val _uiState = MutableStateFlow(StrandEditUiState())
    val uiState: StateFlow<StrandEditUiState> = _uiState.asStateFlow()

    override val _navCommandFlow = MutableSharedFlow<NavCommand>()
    override val navCommandFlow = _navCommandFlow.asSharedFlow()

    override val appUiState: StateFlow<AppUiState> = MutableStateFlow(
        AppUiState(
            hideAppBar = true,
            navigationVisible = true
        )
    ).asStateFlow()

    fun setStrandData(curriculumId: String, strandId: String?) {
        this.curriculumId = curriculumId
        this.strandId = strandId
        _uiState.value = _uiState.value.copy(isEditMode = strandId != null)
        strandId?.let { loadStrand(it) }
    }

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(
            name = name,
            nameError = null
        )
    }

    fun onLearningObjectivesChange(learningObjectives: String) {
        _uiState.value = _uiState.value.copy(
            learningObjectives = learningObjectives,
            learningObjectivesError = null
        )
    }

    fun onOutcomesChange(outcomes: String) {
        _uiState.value = _uiState.value.copy(
            outcomes = outcomes,
            outcomesError = null
        )
    }

    fun onBackClick() {
        viewModelScope.launch {
            _navCommandFlow.emit(NavCommand.Navigate("back"))
        }
    }

    fun onSaveClick() {
        if (validateForm()) {
            saveStrand()
        }
    }

    private fun validateForm(): Boolean {
        val currentState = _uiState.value
        var isValid = true

        val nameError = when {
            currentState.name.isBlank() -> {
                isValid = false
                "Name is required"
            }
            else -> null
        }

        val learningObjectivesError = when {
            currentState.learningObjectives.isBlank() -> {
                isValid = false
                "Learning objectives are required"
            }
            else -> null
        }

        val outcomesError = when {
            currentState.outcomes.isBlank() -> {
                isValid = false
                "Outcomes are required"
            }
            else -> null
        }

        _uiState.value = currentState.copy(
            nameError = nameError,
            learningObjectivesError = learningObjectivesError,
            outcomesError = outcomesError
        )

        return isValid
    }

    private fun saveStrand() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val currentState = _uiState.value

                // TODO: Save strand to repository
                // Simulate successful save
                println("Strand saved: ${currentState.name} for curriculum: $curriculumId")

                _uiState.value = _uiState.value.copy(isLoading = false)
                // Navigate back to curriculum detail screen
                _navCommandFlow.emit(NavCommand.Navigate("back"))

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun loadStrand(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val strand = Strand(
                    id = id,
                    name = "",
                    learningObjectives = "",
                    outcomes = "",
                    isActive = true
                )

                _uiState.value = _uiState.value.copy(
                    strand = strand,
                    name = strand.name,
                    learningObjectives = strand.learningObjectives,
                    outcomes = strand.outcomes,
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