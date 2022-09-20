package com.preonboarding.locationhistory.data.di

import com.preonboarding.locationhistory.data.repository.LocationRepositoryImpl
import com.preonboarding.locationhistory.domain.repository.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun provideLocationRepositoryImpl(repository: LocationRepositoryImpl): LocationRepository
}