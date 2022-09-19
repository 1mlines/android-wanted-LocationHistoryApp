package com.preonboarding.locationhistory.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.preonboarding.locationhistory.data.local.entity.LocationEntity

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationEntity)

    @Query("SELECT * FROM locations WHERE locations.date = :date")
    suspend fun getLocations(date: Long): List<LocationEntity>
}