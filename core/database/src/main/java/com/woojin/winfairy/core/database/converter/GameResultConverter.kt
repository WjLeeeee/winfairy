package com.woojin.winfairy.core.database.converter

import androidx.room.TypeConverter
import com.woojin.winfairy.core.model.GameResult

class GameResultConverter {
    @TypeConverter
    fun fromGameResult(value: GameResult): String = value.name

    @TypeConverter
    fun toGameResult(value: String): GameResult = GameResult.valueOf(value)
}