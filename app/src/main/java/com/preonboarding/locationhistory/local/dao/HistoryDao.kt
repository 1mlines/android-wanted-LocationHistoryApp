package com.preonboarding.locationhistory.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.preonboarding.locationhistory.local.entity.History

@Dao
interface HistoryDao {
    @Query("INSERT INTO history (latitude, longitude) VALUES(:latitude, :longitude)")
    suspend fun insertHistory(latitude: Double, longitude: Double)

    @Query("SELECT DISTINCT latitude, longitude FROM history")
    fun findAll(): List<History>

    @Query("SELECT * FROM history WHERE date(created_at) = :createdAt")
    fun findByCreatedAt(createdAt: String): List<History>
}