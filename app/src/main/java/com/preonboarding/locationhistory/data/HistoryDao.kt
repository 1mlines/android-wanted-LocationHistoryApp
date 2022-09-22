package com.preonboarding.locationhistory.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HISTORY WHERE createdAt = :myDate")
    fun getHistory(myDate: String): LiveData<List<History>>

    @Insert
    fun insertHistory(history: History)

}