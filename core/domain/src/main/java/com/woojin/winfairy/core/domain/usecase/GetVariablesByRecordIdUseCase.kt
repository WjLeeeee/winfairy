package com.woojin.winfairy.core.domain.usecase

import com.woojin.winfairy.core.domain.repository.GameRecordRepository
import com.woojin.winfairy.core.model.GameVariable
import javax.inject.Inject

class GetVariablesByRecordIdUseCase @Inject constructor(
    private val repository: GameRecordRepository
) {
    suspend operator fun invoke(recordId: Long): List<GameVariable> {
        return repository.getVariablesByRecordId(recordId)
    }
}