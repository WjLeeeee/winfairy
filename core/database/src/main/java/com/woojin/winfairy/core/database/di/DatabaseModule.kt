package com.woojin.winfairy.core.database.di

import android.content.Context
import androidx.room.Room
import com.woojin.winfairy.core.database.WinFairyDatabase
import com.woojin.winfairy.core.database.dao.GameRecordDao
import com.woojin.winfairy.core.database.dao.GameVariableDao
import com.woojin.winfairy.core.database.dao.UserTeamDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WinFairyDatabase {
        return Room.databaseBuilder(
            context,
            WinFairyDatabase::class.java,
            "winfairy.db"
        ).build()
    }

    @Provides
    fun provideUserTeamDao(db: WinFairyDatabase): UserTeamDao = db.userTeamDao()

    @Provides
    fun provideGameRecordDao(db: WinFairyDatabase): GameRecordDao = db.gameRecordDao()

    @Provides
    fun provideGameVariableDao(db: WinFairyDatabase): GameVariableDao = db.gameVariableDao()
}