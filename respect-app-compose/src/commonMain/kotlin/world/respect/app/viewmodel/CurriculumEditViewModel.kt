package world.respect.app.viewmodel

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import world.respect.shared.viewmodel.RespectViewModel
import world.respect.app.domain.models.Curriculum
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import world.respect.shared.navigation.NavCommand
import world.respect.shared.viewmodel.app.appstate.AppUiState
import world.respect.app.app.CurriculumDetail

class CurriculumEditViewModel(
    savedStateHandle: SavedStateHandle
) : RespectViewModel(savedStateHandle) {

    private val curriculumId: String? = savedStateHandle.get<String>("curriculumId")

    data class CurriculumEditUiState(
        val curriculum: Curriculum? = null,
        val name: String = "",
        val id: String = "",
        val description: String = "",
        val isLoading: Boolean = false,
        val isEditMode: Boolean = false,
        val nameError: String? = null,
        val idError: String? = null,
        val descriptionError: String? = null,
        val error: String? = null
    ) {
        val isValid: Boolean
            get() = name.isNotBlank() && id.isNotBlank() &&
                    nameError == null && idError == null && descriptionError == null
    }

    private val _uiState = MutableStateFlow(
        CurriculumEditUiState(isEditMode = curriculumId != null)
    )
    val uiState: StateFlow<CurriculumEditUiState> = _uiState.asStateFlow()

    override val _navCommandFlow = MutableSharedFlow<NavCommand>()
    override val navCommandFlow = _navCommandFlow.asSharedFlow()

    override val appUiState: StateFlow<AppUiState> = MutableStateFlow(
        AppUiState(
            hideAppBar = true,
            navigationVisible = true
        )
    ).asStateFlow()

    init {
        curriculumId?.let { loadCurriculum(it) }
    }

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(
            name = name,
            nameError = null
        )
    }

    fun onIdChange(id: String) {
        _uiState.value = _uiState.value.copy(
            id = id,
            idError = null
        )
    }

    fun onDescriptionChange(description: String) {
        _uiState.value = _uiState.value.copy(
            description = description,
            descriptionError = null
        )
    }

    fun onBackClick() {
        viewModelScope.launch {
            _navCommandFlow.emit(NavCommand.Navigate("back"))
        }
    }

    fun onSaveClick() {
        val currentState = _uiState.value

        if (validateForm()) {
            saveCurriculum()
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
        val idError = when {
            currentState.id.isBlank() -> {
                isValid = false
                "ID is required"
            }
            else -> null
        }

        _uiState.value = currentState.copy(
            nameError = nameError,
            idError = idError,
            descriptionError = null
        )

        return isValid
    }

    private fun saveCurriculum() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val currentState = _uiState.value

                _uiState.value = _uiState.value.copy(isLoading = false)

                _navCommandFlow.emit(
                    NavCommand.Navigate(
                        CurriculumDetail(
                            curriculumId = currentState.id,
                            curriculumName = currentState.name
                        )
                    )
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun loadCurriculum(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // TODO: Load curriculum from repository
                // For now, just create empty curriculum for editing
                val curriculum = Curriculum(
                    id = id,
                    name = "",
                    description = "",
                    isActive = true
                )

                _uiState.value = _uiState.value.copy(
                    curriculum = curriculum,
                    name = curriculum.name,
                    id = curriculum.id,
                    description = curriculum.description,
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