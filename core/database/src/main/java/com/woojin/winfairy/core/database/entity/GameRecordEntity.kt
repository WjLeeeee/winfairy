package com.woojin.winfairy.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_record")
data class GameRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,
    val opponentTeam: String,
    val stadium: String,
    val result: String,
    val memo: String? = null,
    val createAt: Long = System.currentTimeMillis()
)
