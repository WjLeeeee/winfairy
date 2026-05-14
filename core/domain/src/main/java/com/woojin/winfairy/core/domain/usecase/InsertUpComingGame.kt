package com.woojin.winfairy.core.domain.usecase

import com.woojin.winfairy.core.domain.repository.UpComingGameRepository
import com.woojin.winfairy.core.model.UpcomingGame
import javax.inject.Inject

class InsertUpComingGame @Inject constructor(
    private val repository: UpComingGameRepository
) {
    suspend operator fun invoke(game: UpcomingGame) {
        repository.insertUpComingGame(game = game)
    }
}