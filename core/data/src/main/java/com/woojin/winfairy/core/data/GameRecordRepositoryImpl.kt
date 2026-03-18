package com.woojin.winfairy.core.data

import com.woojin.winfairy.core.data.mapper.toDomain
import com.woojin.winfairy.core.database.dao.GameRecordDao
import com.woojin.winfairy.core.domain.repository.GameRecordRepository
import com.woojin.winfairy.core.model.GameRecord
import javax.inject.Inject

class GameRecordRepositoryImpl @Inject constructor(
    private val gameRecordDao: GameRecordDao
) : GameRecordRepository {
    override suspend fun getAllRecord(): List<GameRecord> {
        return gameRecordDao.getAllRecords().map { it.toDomain() }
    }
}