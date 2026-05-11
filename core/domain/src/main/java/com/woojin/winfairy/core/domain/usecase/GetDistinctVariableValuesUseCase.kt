package com.woojin.winfairy.core.domain.usecase

import com.woojin.winfairy.core.domain.repository.GameRecordRepository
import javax.inject.Inject

class GetDistinctVariableValuesUseCase @Inject constructor(
    private val repository: GameRecordRepository
) {
    suspend operator fun invoke(category: String): List<String> {
        return repository.getDistinctVariableValues(category)
    }
}