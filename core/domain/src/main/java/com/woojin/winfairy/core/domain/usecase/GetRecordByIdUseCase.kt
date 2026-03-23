package com.woojin.winfairy.core.domain.usecase

import com.woojin.winfairy.core.domain.repository.GameRecordRepository
import com.woojin.winfairy.core.model.GameRecord
import javax.inject.Inject

class GetRecordByIdUseCase @Inject constructor(
    private val repository: GameRecordRepository
) {
    suspend operator fun invoke(id: Long): GameRecord? {
        return repository.getRecordById(id)
    }
}