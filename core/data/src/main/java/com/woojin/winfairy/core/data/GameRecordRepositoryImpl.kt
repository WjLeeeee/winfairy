package com.woojin.winfairy.core.data

import androidx.lifecycle.liveData
import com.woojin.winfairy.core.data.mapper.toDomain
import com.woojin.winfairy.core.data.mapper.toEntity
import com.woojin.winfairy.core.database.dao.GameRecordDao
import com.woojin.winfairy.core.database.dao.GameVariableDao
import com.woojin.winfairy.core.domain.repository.GameRecordRepository
import com.woojin.winfairy.core.model.GameRecord
import com.woojin.winfairy.core.model.GameRecordWithVariables
import com.woojin.winfairy.core.model.GameVariable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GameRecordRepositoryImpl @Inject constructor(
    private val gameRecordDao: GameRecordDao,
    private val gameVariableDao: GameVariableDao
) : GameRecordRepository {
    override fun getAllRecord(): Flow<List<GameRecord>> {
        return gameRecordDao.getAllRecords().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addRecord(record: GameRecord, variables: List<GameVariable>) {
        val recordId = gameRecordDao.insertRecord(record.toEntity())
        if (variables.isNotEmpty()) {
            gameVariableDao.insertVariables(
                variables.map { it.toEntity(gameRecordId = recordId) }
            )
        }
    }

    override suspend fun getRecordById(id: Long): GameRecord? {
        return gameRecordDao.getRecordById(id)?.toDomain()
    }

    override suspend fun getVariablesByRecordId(recordId: Long): List<GameVariable> {
        return gameVariableDao.getVariablesByRecordId(recordId).map {
            GameVariable(category = it.category, value = it.value)
        }
    }

    override suspend fun updateRecord(record: GameRecord, variables: List<GameVariable>) {
        gameRecordDao.updateRecord(record.toEntity())
        gameVariableDao.deleteVariablesByRecordId(record.id)
        if (variables.isNotEmpty()) {
            gameVariableDao.insertVariables(
                variables.map { it.toEntity(gameRecordId = record.id) }
            )
        }
    }

    override suspend fun deleteRecord(recordId: Long) {
        val deleteRecordById = gameRecordDao.getRecordById(recordId) ?: return
        gameVariableDao.deleteVariablesByRecordId(recordId)
        gameRecordDao.deleteRecord(deleteRecordById)
    }

    override fun getAllRecordsWithVariablesFlow(): Flow<List<GameRecordWithVariables>> {
        return gameRecordDao.getAllRecords().map { entities ->
            entities.map { entity ->
                val variables = gameVariableDao.getVariablesByRecordId(entity.id)
                GameRecordWithVariables(
                    record = entity.toDomain(),
                    variables = variables.map { GameVariable(it.category, it.value) }
                )
            }
        }
    }
}