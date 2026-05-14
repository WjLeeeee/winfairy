package com.woojin.winfairy.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.woojin.winfairy.core.database.converter.GameResultConverter
import com.woojin.winfairy.core.database.dao.GameRecordDao
import com.woojin.winfairy.core.database.dao.GameVariableDao
import com.woojin.winfairy.core.database.dao.UpComingGameDao
import com.woojin.winfairy.core.database.dao.UserTeamDao
import com.woojin.winfairy.core.database.entity.GameRecordEntity
import com.woojin.winfairy.core.database.entity.GameVariableEntity
import com.woojin.winfairy.core.database.entity.UpComingGameEntity
import com.woojin.winfairy.core.database.entity.UserTeamEntity

@Database(
    entities = [
        GameRecordEntity::class,
        UpComingGameEntity::class,
        GameVariableEntity::class,
        UserTeamEntity::class,
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(GameResultConverter::class)
abstract class WinFairyDatabase : RoomDatabase() {
    abstract fun gameRecordDao(): GameRecordDao
    abstract fun upComingGameDao(): UpComingGameDao
    abstract fun gameVariableDao(): GameVariableDao
    abstract fun userTeamDao(): UserTeamDao
}