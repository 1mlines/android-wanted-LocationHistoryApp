package com.preonboarding.locationhistory.feature.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.base.BaseActivity
import com.preonboarding.locationhistory.databinding.ActivitySplashBinding
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.timer

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        activityScope.launch {
            delay(3000)
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }
}