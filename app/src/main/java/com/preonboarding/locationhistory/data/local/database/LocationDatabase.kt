package com.preonboarding.locationhistory.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.preonboarding.locationhistory.data.local.dao.LocationDao
import com.preonboarding.locationhistory.data.local.entity.LocationEntity

@Database(entities = [LocationEntity::class], version = 1, exportSchema = false)
abstract class LocationDatabase: RoomDatabase() {
    abstract fun locationDao(): LocationDao
}