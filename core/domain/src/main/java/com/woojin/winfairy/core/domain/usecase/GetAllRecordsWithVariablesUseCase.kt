package com.woojin.winfairy.core.domain.usecase

import com.woojin.winfairy.core.domain.repository.GameRecordRepository
import com.woojin.winfairy.core.model.GameRecordWithVariables
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllRecordsWithVariablesUseCase @Inject constructor(
    private val repository: GameRecordRepository
) {
    operator fun invoke(): Flow<List<GameRecordWithVariables>> {
        return repository.getAllRecordsWithVariablesFlow()
    }
}