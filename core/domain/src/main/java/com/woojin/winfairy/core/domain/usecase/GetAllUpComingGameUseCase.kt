package com.woojin.winfairy.core.domain.usecase

import com.woojin.winfairy.core.domain.repository.UpComingGameRepository
import com.woojin.winfairy.core.model.UpcomingGame
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllUpComingGameUseCase @Inject constructor(
    private val repository: UpComingGameRepository
) {
    operator fun invoke(): Flow<List<UpcomingGame>> {
        return repository.getAllUpComingGame()
    }
}