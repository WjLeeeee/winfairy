package com.woojin.winfairy.core.domain.usecase

import com.woojin.winfairy.core.domain.repository.GameRecordRepository
import com.woojin.winfairy.core.model.GameRecord
import com.woojin.winfairy.core.model.GameVariable
import javax.inject.Inject

class AddGameRecordUseCase @Inject constructor(
    private val repository: GameRecordRepository
) {
    suspend operator fun invoke(record: GameRecord, variables: List<GameVariable>) {
        repository.addRecord(record, variables)
    }
}