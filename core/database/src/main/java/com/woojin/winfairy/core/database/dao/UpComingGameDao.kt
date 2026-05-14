package com.woojin.winfairy.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.woojin.winfairy.core.database.entity.UpComingGameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UpComingGameDao {
    @Query("SELECT * FROM up_coming_game ORDER BY date DESC")
    fun getAllUpComingGame(): Flow<List<UpComingGameEntity>>

    @Query("SELECT * FROM up_coming_game WHERE id = :id")
    suspend fun getUpComingGameById(id: Long): UpComingGameEntity?
    @Insert
    suspend fun insertUpComingGame(game: UpComingGameEntity)

    @Delete
    suspend fun deleteUpComingGame(game: UpComingGameEntity)
}