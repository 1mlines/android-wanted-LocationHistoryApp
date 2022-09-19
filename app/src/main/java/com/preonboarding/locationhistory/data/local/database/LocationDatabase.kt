package com.preonboarding.locationhistory.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.preonboarding.locationhistory.data.local.dao.LocationDao
import com.preonboarding.locationhistory.data.local.entity.LocationEntity

@Database(entities = [LocationEntity::class], version = 1, exportSchema = false)
abstract class LocationDatabase: RoomDatabase() {
    abstract fun locationDao(): LocationDao

    companion object {
        private var INSTANCE: LocationDatabase? = null

        @Synchronized
        fun getInstance(context: Context): LocationDatabase? {
            if (INSTANCE == null) {
                synchronized(LocationDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    LocationDatabase::class.java, "location.db")
                        .build()
                }
            }
            return INSTANCE
        }
    }
}