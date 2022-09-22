package com.preonboarding.locationhistory.local.dao

import android.util.Log
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.preonboarding.locationhistory.local.database.AppDatabase
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@SmallTest
class HistoryDaoTest {
    private lateinit var appDatabase: AppDatabase

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun historyDaoTest() {
        val latitude = 3.3
        val longitude = 4.4
        val createdAt = "2022-09-22" //today's yyyy-mm-dd
        appDatabase.historyDao().insertHistory(latitude, longitude)

        var findAll = appDatabase.historyDao().findAll()
        Log.d("findAll", findAll.toString())
        assertThat(latitude, equalTo(findAll[0].latitude))

        var findByCreatedAt = appDatabase.historyDao().findByCreatedAt(createdAt)
        Log.d("findByCreatedAt", findByCreatedAt.toString())
        assertThat(latitude, equalTo(findByCreatedAt[0].latitude))
    }
}