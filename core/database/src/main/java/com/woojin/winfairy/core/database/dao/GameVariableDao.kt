package com.woojin.winfairy.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.woojin.winfairy.core.database.entity.GameVariableEntity

@Dao
interface GameVariableDao {

    @Query("SELECT * FROM game_variable WHERE gameRecordId = :gameRecordId")
    suspend fun getVariablesByRecordId(gameRecordId: Long): List<GameVariableEntity>

    @Insert
    suspend fun insertVariables(variables: List<GameVariableEntity>)

    @Query("DELETE FROM game_variable WHERE gameRecordId = :gameRecordId")
    suspend fun deleteVariablesByRecordId(gameRecordId: Long)
}