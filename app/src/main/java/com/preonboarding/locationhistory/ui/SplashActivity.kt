package com.preonboarding.locationhistory.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.preonboarding.locationhistory.R

class SplashActivity : AppCompatActivity() {
    private val handler = Handler()
    private val delay = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        delaySplashScreen()
    }

    fun delaySplashScreen() {
        handler.postDelayed({
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }, delay)
    }
}