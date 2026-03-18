package com.woojin.winfairy.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_team")
data class UserTeamEntity(
    @PrimaryKey
    val id: Long = 0,      // 항상 1개만 저장
    val teamCode: String,  // KboTeam.name
)