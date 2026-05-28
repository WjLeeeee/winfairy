package com.woojin.winfairy.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.woojin.winfairy.core.database.WinFairyDatabase
import com.woojin.winfairy.core.database.dao.GameRecordDao
import com.woojin.winfairy.core.database.dao.GameVariableDao
import com.woojin.winfairy.core.database.dao.UpComingGameDao
import com.woojin.winfairy.core.database.dao.UserTeamDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
                CREATE TABLE IF NOT EXISTS `up_coming_game` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `date` TEXT NOT NULL,
                    `opponentTeam` TEXT NOT NULL,
                    `stadium` TEXT NOT NULL
                )
            """
        )
    }
}
private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE game_record ADD COLUMN homeScore INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE game_record ADD COLUMN awayScore INTEGER NOT NULL DEFAULT 0")
    }
}

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
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3).build()
    }

    @Provides
    fun provideUserTeamDao(db: WinFairyDatabase): UserTeamDao = db.userTeamDao()

    @Provides
    fun provideGameRecordDao(db: WinFairyDatabase): GameRecordDao = db.gameRecordDao()

    @Provides
    fun provideUpComingGameDao(db: WinFairyDatabase): UpComingGameDao = db.upComingGameDao()

    @Provides
    fun provideGameVariableDao(db: WinFairyDatabase): GameVariableDao = db.gameVariableDao()
}