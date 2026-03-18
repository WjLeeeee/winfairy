package com.woojin.winfairy.core.data.di

import com.woojin.winfairy.core.data.UserTeamRepositoryImpl
import com.woojin.winfairy.core.domain.UserTeamRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUserTeamRepository(
        impl: UserTeamRepositoryImpl
    ): UserTeamRepository
}