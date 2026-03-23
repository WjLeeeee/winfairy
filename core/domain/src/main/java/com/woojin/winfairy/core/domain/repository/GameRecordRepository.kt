package com.woojin.winfairy.core.domain.repository

import com.woojin.winfairy.core.model.GameRecord
import com.woojin.winfairy.core.model.GameVariable
import kotlinx.coroutines.flow.Flow

interface GameRecordRepository {
    fun getAllRecord(): Flow<List<GameRecord>>
    suspend fun addRecord(record: GameRecord, variables: List<GameVariable>)
}