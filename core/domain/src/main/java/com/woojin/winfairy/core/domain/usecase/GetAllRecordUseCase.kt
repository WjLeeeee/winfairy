package com.woojin.winfairy.core.domain.usecase

import com.woojin.winfairy.core.domain.repository.GameRecordRepository
import com.woojin.winfairy.core.model.GameRecord
import javax.inject.Inject

class GetAllRecordUseCase @Inject constructor(
    private val repository: GameRecordRepository
) {
    suspend operator fun invoke(): List<GameRecord> {
        return repository.getAllRecord()
    }
}