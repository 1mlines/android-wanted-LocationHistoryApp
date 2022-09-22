package com.preonboarding.locationhistory.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.preonboarding.locationhistory.local.dao.HistoryDao
import com.preonboarding.locationhistory.local.entity.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}