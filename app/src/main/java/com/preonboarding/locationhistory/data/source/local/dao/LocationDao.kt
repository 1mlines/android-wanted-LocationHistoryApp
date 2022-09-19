package com.preonboarding.locationhistory.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.preonboarding.locationhistory.data.source.local.entity.LocationEntity

/**
 * @Created by 김현국 2022/09/19
 */

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(location: LocationEntity): Long

    @Query("SELECT * FROM locations WHERE locations.date = :date")
    suspend fun getLocationsWithDate(date: String): List<LocationEntity>
}
