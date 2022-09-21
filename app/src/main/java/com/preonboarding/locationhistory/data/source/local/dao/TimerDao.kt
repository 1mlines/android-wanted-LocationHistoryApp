package com.preonboarding.locationhistory.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.preonboarding.locationhistory.data.source.local.entity.TimerEntity

/**
 * @Created by 김현국 2022/09/20
 */
@Dao
interface TimerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(timerEntity: TimerEntity): Long

    @Query("SELECT currentDuration FROM timer WHERE timer.id = :id limit 1")
    suspend fun getDuration(id: Int): Long?
}
