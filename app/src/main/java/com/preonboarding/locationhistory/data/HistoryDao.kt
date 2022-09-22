package com.preonboarding.locationhistory.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HISTORY WHERE DATE(datetime) = DATE(:myDate)")
    fun getHistory(myDate: Date): Flow<List<History>>

    @Insert
    fun insertHistory(myHistory: History)

}