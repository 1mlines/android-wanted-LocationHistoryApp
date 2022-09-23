package com.preonboarding.locationhistory.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.preonboarding.locationhistory.data.source.local.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

/**
 * @Created by 김현국 2022/09/19
 */

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(location: LocationEntity): Long

    @Query("SELECT * FROM locations WHERE locations.date like :date")
    suspend fun getLocationsWithDate(date: String): List<LocationEntity>

    @Query("SELECT * FROM locations")
    fun getLocations(): Flow<List<LocationEntity>>
}
