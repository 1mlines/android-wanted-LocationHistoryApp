package com.preonboarding.locationhistory.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.preonboarding.locationhistory.data.db.dao.LocationDao
import com.preonboarding.locationhistory.data.db.entity.LocationEntity

@Database(entities = [LocationEntity::class], version = 1, exportSchema = false)
abstract class LocationDatabase: RoomDatabase() {
    abstract fun locationDao(): LocationDao
}