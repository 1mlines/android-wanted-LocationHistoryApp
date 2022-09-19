package com.preonboarding.locationhistory.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.preonboarding.locationhistory.data.source.local.entity.LocationEntity

/**
 * @Created by 김현국 2022/09/19
 */

@Database(
    entities = [LocationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabaseImpl : RoomDatabase(), AppDatabase {

    companion object {
        const val DB_NAME = "AppDatabase.db"
    }
}
