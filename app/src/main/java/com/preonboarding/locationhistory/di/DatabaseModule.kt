package com.preonboarding.locationhistory.di

import android.content.Context
import androidx.room.Room
import com.preonboarding.locationhistory.data.source.local.dao.LocationDao
import com.preonboarding.locationhistory.data.source.local.dao.TimerDao
import com.preonboarding.locationhistory.data.source.local.database.AppDatabase
import com.preonboarding.locationhistory.data.source.local.database.AppDatabaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @Created by 김현국 2022/09/19
 */

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabaseImpl::class.java,
        AppDatabaseImpl.DB_NAME
    ).build()

    @Singleton
    @Provides
    fun provideLocationDao(appDatabase: AppDatabase): LocationDao = appDatabase.locationDao()

    @Singleton
    @Provides
    fun provideTimerDao(appDatabase: AppDatabase): TimerDao = appDatabase.timerDao()
}
