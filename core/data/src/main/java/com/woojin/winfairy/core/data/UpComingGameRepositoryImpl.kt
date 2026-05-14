package com.woojin.winfairy.core.data

import com.woojin.winfairy.core.data.mapper.toDomain
import com.woojin.winfairy.core.data.mapper.toEntity
import com.woojin.winfairy.core.database.dao.UpComingGameDao
import com.woojin.winfairy.core.domain.repository.UpComingGameRepository
import com.woojin.winfairy.core.model.UpcomingGame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UpComingGameRepositoryImpl @Inject constructor(
    private val upComingGameDao: UpComingGameDao,
) : UpComingGameRepository {
    override fun getAllUpComingGame(): Flow<List<UpcomingGame>> {
        return upComingGameDao.getAllUpComingGame().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertUpComingGame(game: UpcomingGame) {
        upComingGameDao.insertUpComingGame(game.toEntity())
    }

    override suspend fun getUpComingGameById(id: Long): UpcomingGame? {
        return upComingGameDao.getUpComingGameById(id)?.toDomain()
    }

    override suspend fun deleteUpComingGame(id: Long) {
        val deleteGameId = upComingGameDao.getUpComingGameById(id) ?: return
        upComingGameDao.deleteUpComingGame(deleteGameId)
    }
}