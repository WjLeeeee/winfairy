package com.woojin.winfairy.core.domain.usecase

import com.woojin.winfairy.core.domain.repository.GameRecordRepository
import javax.inject.Inject

class DeleteGameRecordUseCase @Inject constructor(
    private val repository: GameRecordRepository
) {
    suspend operator fun invoke(recordId: Long) {
        repository.deleteRecord(recordId)
    }
}