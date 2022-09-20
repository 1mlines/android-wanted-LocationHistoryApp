package com.preonboarding.locationhistory.presentation.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.data.model.Location
import com.preonboarding.locationhistory.databinding.ActivityMainBinding
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val timestamp = System.currentTimeMillis()
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val location = Location(id = 0, latitude = 37.553305f, longitude = 126.972675f, date = timestamp)
        viewModel.insert(location)
        viewModel.getLocation(timestamp)
    }
}