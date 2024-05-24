package tr.com.helpark.core.domain

interface UseCase<T: UiModel, out E> {
    suspend operator fun invoke(
        params: UseCaseParams? = null
    ): UiResult<T, E>
}

interface UseCaseParams

interface UiModel