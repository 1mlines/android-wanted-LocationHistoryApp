package com.preonboarding.locationhistory.data

import androidx.room.*
import java.util.*

@Dao
interface LocationDao {
    @Query("SELECT * FROM LOCATIONITEM WHERE DATE(datetime) = DATE(:myDate)")
            fun getDate(myDate:Date)

    @Insert
    fun insert(locationItem: LocationItem)
    @Update
    fun update(locationItem: LocationItem)
    @Delete
    fun delete(locationItem: LocationItem)

}