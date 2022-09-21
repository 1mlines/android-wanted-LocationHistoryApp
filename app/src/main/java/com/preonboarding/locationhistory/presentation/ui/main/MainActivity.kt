package com.preonboarding.locationhistory.presentation.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.databinding.ActivityMainBinding
import com.preonboarding.locationhistory.presentation.WebViewBridge
import com.preonboarding.locationhistory.presentation.ui.BaseActivity
import com.preonboarding.locationhistory.util.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()
    private val webViewBridge: WebViewBridge by lazy {
        WebViewBridge(binding.webView, WeakReference(Handler(Looper.myLooper()!!)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel

        checkPermission()
    }

    fun initWebView() {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback?
            ) {
                super.onGeolocationPermissionsShowPrompt(origin, callback)
                callback?.invoke(origin, true, false)
            }
        }
        binding.webView.addJavascriptInterface(webViewBridge, "Android")
        binding.webView.loadUrl("file:///android_asset/map.html")
    }

    @SuppressLint("MissingPermission")
    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            initWebView()
            return
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (PermissionUtils.isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) && PermissionUtils.isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            checkPermission()
        } else {
            Toast.makeText(this, "위치권한을 설정해주세요.", Toast.LENGTH_SHORT).show()
        }

    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}

