package world.respect.shared.domain.curriculum

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.delay
import world.respect.shared.domain.curriculum.models.Curriculum

interface GetCurriculaUseCase {
    suspend operator fun invoke(): Flow<List<Curriculum>>
}

interface GetCurriculumByIdUseCase {
    suspend operator fun invoke(curriculumId: String): Curriculum?
}

interface SaveCurriculumUseCase {
    suspend operator fun invoke(curriculum: Curriculum): Result<Curriculum>
}

interface DeleteCurriculumUseCase {
    suspend operator fun invoke(curriculumId: String): Result<Unit>
}


object CurriculumMockData {
    val curricula = MutableStateFlow<List<Curriculum>>(
        listOf(
            Curriculum("1", "Mathematics", "Basic mathematics curriculum", true),
            Curriculum("2", "English", "Basic english curriculum", true)
        )
    )
}

class GetCurriculaUseCaseMock : GetCurriculaUseCase {
    override suspend operator fun invoke(): Flow<List<Curriculum>> {
        return CurriculumMockData.curricula
    }
}

class GetCurriculumByIdUseCaseMock : GetCurriculumByIdUseCase {
    override suspend operator fun invoke(curriculumId: String): Curriculum? {
        return CurriculumMockData.curricula.value.find { it.id == curriculumId }
    }
}

class SaveCurriculumUseCaseMock : SaveCurriculumUseCase {
    override suspend operator fun invoke(curriculum: Curriculum): Result<Curriculum> {
        return try {
            require(curriculum.name.isNotBlank()) { "" }
            require(curriculum.id.isNotBlank()) { "" }

            delay(500L)

            val currentList = CurriculumMockData.curricula.value.toMutableList()
            val existingIndex = currentList.indexOfFirst { it.id == curriculum.id }

            if (existingIndex >= 0) {
                currentList[existingIndex] = curriculum
            } else {
                currentList.add(curriculum)
            }

            CurriculumMockData.curricula.value = currentList
            Result.success(curriculum)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class DeleteCurriculumUseCaseMock : DeleteCurriculumUseCase {
    override suspend operator fun invoke(curriculumId: String): Result<Unit> {
        return try {
            require(curriculumId.isNotBlank()) { "" }

            delay(300L)

            val currentList = CurriculumMockData.curricula.value.toMutableList()
            val removed = currentList.removeAll { it.id == curriculumId }

            if (removed) {
                CurriculumMockData.curricula.value = currentList
                world.respect.shared.domain.strand.StrandMockData.removeStrandsByCurriculumId(curriculumId)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}