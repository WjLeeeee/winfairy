package com.woojin.winfairy.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.woojin.winfairy.core.database.entity.UserTeamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserTeamDao {
    @Query("SELECT * FROM user_team WHERE id = 0")
    suspend fun getUserTeam(): UserTeamEntity?

    @Query("SELECT * FROM user_team WHERE id = 0")
    fun getUserTeamFlow(): Flow<UserTeamEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setUserTeam(userTeam: UserTeamEntity)
}