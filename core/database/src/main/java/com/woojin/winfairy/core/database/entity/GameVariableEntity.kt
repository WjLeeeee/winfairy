package com.woojin.winfairy.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_variable")
data class GameVariableEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val gameRecordId: Long,
    val category: String,
    val value: String,
)

