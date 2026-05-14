package com.woojin.winfairy.core.domain.usecase

import com.woojin.winfairy.core.domain.repository.UpComingGameRepository
import com.woojin.winfairy.core.model.UpcomingGame
import javax.inject.Inject

class GetUpComingGameByIdUseCase @Inject constructor(
    private val repository: UpComingGameRepository
) {
    suspend operator fun invoke(id: Long): UpcomingGame? {
        return repository.getUpComingGameById(id)
    }
}