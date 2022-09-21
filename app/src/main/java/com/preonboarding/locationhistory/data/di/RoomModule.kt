package com.preonboarding.locationhistory.data.di

import android.content.Context
import androidx.room.Room
import com.preonboarding.locationhistory.data.db.database.LocationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun getDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        LocationDatabase::class.java,
        "location.db"
    ).build()

    @Singleton
    @Provides
    fun getLocationDao(database: LocationDatabase) = database.locationDao()
}