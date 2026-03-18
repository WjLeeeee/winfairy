package com.woojin.winfairy.core.domain.usecase

import com.woojin.winfairy.core.model.GameRecord
import com.woojin.winfairy.core.model.GameResult
import javax.inject.Inject

class GetWinRateUseCase @Inject constructor() {
    operator fun invoke(records: List<GameRecord>): Float {
        val validRecords = records.filter { it.result != GameResult.CANCELED }
        if (validRecords.isEmpty()) return 0f
        val wins = validRecords.count { it.result == GameResult.WIN }
        return wins.toFloat() / validRecords.size * 100
    }
}