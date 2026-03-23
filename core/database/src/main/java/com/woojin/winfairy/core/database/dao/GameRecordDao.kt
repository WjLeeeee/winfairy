package com.woojin.winfairy.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.woojin.winfairy.core.database.entity.GameRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameRecordDao {

    @Query("SELECT * FROM game_record ORDER BY date DESC")
    fun getAllRecords(): Flow<List<GameRecordEntity>>

    @Query("SELECT * FROM game_record WHERE id = :id")
    suspend fun getRecordById(id: Long): GameRecordEntity?

    @Insert
    suspend fun insertRecord(record: GameRecordEntity): Long

    @Update
    suspend fun updateRecord(record: GameRecordEntity)

    @Delete
    suspend fun deleteRecord(record: GameRecordEntity)
}