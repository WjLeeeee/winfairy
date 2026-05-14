package com.woojin.winfairy.core.domain.usecase

import com.woojin.winfairy.core.domain.repository.UpComingGameRepository
import javax.inject.Inject

class DeleteUpComingGameUseCase @Inject constructor(
    private val repository: UpComingGameRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteUpComingGame(id)
    }
}