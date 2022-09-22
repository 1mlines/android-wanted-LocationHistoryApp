package com.preonboarding.locationhistory.local.dao

import android.util.Log
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.preonboarding.locationhistory.local.database.AppDatabase
import com.preonboarding.locationhistory.local.entity.History
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
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
        assertEquals("com.preonboarding.locationhistory", appContext.packageName)
        appDatabase = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun historyDaoTest() {
        val history = History(latitude = 3.3, longitude = 3.3)
        appDatabase.historyDao().insertHistory(latitude = 3.3, longitude = 3.3)

//        var historyByDistance = appDatabase.historyDao().findDistinctByDistance(distance = 0.2)[0]
//        Log.d("historyByDistance", historyByDistance.toString())
//        assertThat(history.latitude, equalTo(historyByDistance.latitude))

//        var historyByDistanceAndCreatedAt = appDatabase.historyDao()
//            .findHistoryByDistanceAndCreatedAt(
//                distance = 0.2,
//                createdAt = LocalDate.now().toString()
//            )[0]
//        Log.d("now", LocalDate.now().toString())
//        Log.d("historyByDistanceAndCreatedAt", historyByDistanceAndCreatedAt.toString())
//        assertThat(history.latitude, equalTo(historyByDistanceAndCreatedAt.latitude))
    }
}