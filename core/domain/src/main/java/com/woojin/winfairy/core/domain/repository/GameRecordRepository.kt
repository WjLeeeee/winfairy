package com.woojin.winfairy.core.domain.repository

import com.woojin.winfairy.core.model.GameRecord
import com.woojin.winfairy.core.model.GameVariable
import kotlinx.coroutines.flow.Flow

interface GameRecordRepository {
    fun getAllRecord(): Flow<List<GameRecord>> //홈에서 collect 하는 용도
    suspend fun addRecord(record: GameRecord, variables: List<GameVariable>) //최초 기록 추가
    suspend fun getRecordById(id: Long): GameRecord? //기록 수정 에서 사용
    suspend fun getVariablesByRecordId(recordId: Long): List<GameVariable> //기록 수정 에서 사용
    suspend fun updateRecord(record: GameRecord, variables: List<GameVariable>) //기록 수정 에서 사용
    suspend fun deleteRecord(recordId: Long) //기록 삭제
}