package com.preonboarding.locationhistory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LocationItem::class], version = 1)
abstract class LocationDB : RoomDatabase() {
    abstract fun locationDao(): LocationDao

    companion object{
        private var instance: LocationDB?= null

        @Synchronized
        fun getInstance(context: Context): LocationDB?{
            if (instance == null){
                synchronized(LocationDB::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LocationDB::class.java,
                        "locationDB"
                    ).allowMainThreadQueries().build()
                }
            }
        return instance
        }

    }
}