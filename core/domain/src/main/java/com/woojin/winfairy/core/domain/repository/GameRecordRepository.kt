package com.woojin.winfairy.core.domain.repository

import com.woojin.winfairy.core.model.GameRecord

interface GameRecordRepository {
    suspend fun getAllRecord(): List<GameRecord>
}