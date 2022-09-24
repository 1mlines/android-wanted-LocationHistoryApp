package com.preonboarding.locationhistory.data.source.local.database

import com.preonboarding.locationhistory.data.source.local.dao.LocationDao
import com.preonboarding.locationhistory.data.source.local.dao.TimerDao

/**
 * @Created by 김현국 2022/09/19
 */
interface AppDatabase {

    fun locationDao(): LocationDao

    fun timerDao(): TimerDao
}
