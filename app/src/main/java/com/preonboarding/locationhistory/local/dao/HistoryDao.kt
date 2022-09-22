package com.preonboarding.locationhistory.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.preonboarding.locationhistory.local.entity.History

@Dao
interface HistoryDao {
    @Query("INSERT INTO history (latitude, longitude) VALUES(:latitude, :longitude)")
    fun insertHistory(latitude: Double, longitude: Double)


    @Query("SELECT DISTINCT latitude, longitude FROM history")
    fun findAll(): List<History>

    /*
    * createdAt 형태가 0000-00-00 00:00:00인데 날짜비교 할때 시간 빼고 0000-00-00만 비교해야하기 때문에
    * String으로 사용하면 비교가 어려울 것 같고 @TypeConverter 이용해서 Long으로 써야할 것 같죠..?
    */
    @Query("SELECT * FROM history WHERE date(created_at) = :createdAt")
    fun findByCreatedAt(createdAt: String): List<History>
}