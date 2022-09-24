package com.preonboarding.locationhistory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.preonboarding.locationhistory.WantedApplication.Companion.getAppContext

@Database(entities = [History::class], version = 1)
abstract class HistoryDB : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        private var instance: HistoryDB? = null

        @Synchronized
        fun getDatabase(context: Context): HistoryDB? {
            if (instance == null) {
                synchronized(HistoryDB::class) {
                    instance = Room.databaseBuilder(
                        getAppContext(), HistoryDB::class.java, "history_db"
                    ).build()
                }

            }
            return instance
        }
    }

}
