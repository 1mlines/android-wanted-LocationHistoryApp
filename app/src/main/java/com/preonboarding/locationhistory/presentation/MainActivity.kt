package com.preonboarding.locationhistory.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mapFragment: MapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initMap()
        bindViews()
    }

    private fun initMap() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.naverMapFragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                supportFragmentManager.beginTransaction()
                    .add(R.id.naverMapFragment, it).commit()
            }

        mapFragment.getMapAsync(this)
    }


    private fun bindViews() = with(binding) {
        addressButton.setOnClickListener {
            // TODO
        }

        historyButton.setOnClickListener {
            // TODO
        }

        settingsButton.setOnClickListener {
            // TODO
        }
    }


    override fun onMapReady(naverMap: NaverMap) {

    }
}