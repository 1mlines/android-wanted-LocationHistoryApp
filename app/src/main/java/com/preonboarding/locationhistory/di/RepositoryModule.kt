package com.preonboarding.locationhistory.di

import com.preonboarding.locationhistory.data.repository.LocationRepository
import com.preonboarding.locationhistory.data.source.local.datasource.LocationDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @Created by 김현국 2022/09/19
 */

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideLocationRepository(locationDataSource: LocationDataSource): LocationRepository {
        return LocationRepository(locationDataSource = locationDataSource)
    }
}
