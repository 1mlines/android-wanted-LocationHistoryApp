package com.preonboarding.locationhistory.data.di

import com.preonboarding.locationhistory.data.repository.LocationRepositoryImpl
import com.preonboarding.locationhistory.data.repository.StorageIntervalRepositoryImpl
import com.preonboarding.locationhistory.domain.repository.LocationRepository
import com.preonboarding.locationhistory.domain.repository.StorageIntervalRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun provideLocationRepositoryImpl(repository: LocationRepositoryImpl): LocationRepository

    @Binds
    @Singleton
    fun provideStorageIntervalRepositoryImpl(repository: StorageIntervalRepositoryImpl): StorageIntervalRepository
}
