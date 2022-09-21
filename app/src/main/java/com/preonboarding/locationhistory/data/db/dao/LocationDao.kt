package com.preonboarding.locationhistory.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.preonboarding.locationhistory.data.db.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationEntity)

    @Query("SELECT * FROM locations WHERE date = :date")
    suspend fun getLocations(date: Long): List<LocationEntity>

    @Query("SELECT * FROM locations ")
    fun getLocations(): Flow<List<LocationEntity>>
}