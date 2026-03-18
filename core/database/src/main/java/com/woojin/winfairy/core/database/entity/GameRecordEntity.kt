package com.woojin.winfairy.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.woojin.winfairy.core.model.GameResult

@Entity(tableName = "game_record")
data class GameRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,
    val opponentTeam: String,
    val stadium: String,
    val result: GameResult,
    val memo: String? = null,
    val createAt: Long = System.currentTimeMillis()
)
