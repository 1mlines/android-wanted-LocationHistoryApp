package com.preonboarding.locationhistory.view

import android.os.Bundle
import android.widget.Toast
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.base.BaseActivity
import com.preonboarding.locationhistory.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnAddress.setOnClickListener {
            Toast.makeText(this, "주소버튼을 눌렀습니다", Toast.LENGTH_SHORT).show()
        }
    }
}