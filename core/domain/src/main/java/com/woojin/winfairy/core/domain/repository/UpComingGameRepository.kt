package com.woojin.winfairy.core.domain.repository

import com.woojin.winfairy.core.model.UpcomingGame
import kotlinx.coroutines.flow.Flow

interface UpComingGameRepository {
    fun getAllUpComingGame(): Flow<List<UpcomingGame>>
    suspend fun insertUpComingGame(game: UpcomingGame)
    suspend fun getUpComingGameById(id: Long): UpcomingGame?
    suspend fun deleteUpComingGame(id: Long)
}