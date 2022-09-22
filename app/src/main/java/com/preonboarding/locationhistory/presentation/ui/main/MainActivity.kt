package com.preonboarding.locationhistory.presentation.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewTreeObserver
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.gson.Gson
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.databinding.ActivityMainBinding
import com.preonboarding.locationhistory.presentation.WebViewBridge
import com.preonboarding.locationhistory.presentation.base.BaseActivity
import com.preonboarding.locationhistory.presentation.ui.setting.SettingDialog
import com.preonboarding.locationhistory.util.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.concurrent.thread

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()

    private var isReady = false

    @Inject
    lateinit var gson: Gson

    private val webViewBridge: WebViewBridge by lazy {
        WebViewBridge(
            gson = gson,
            webView = binding.webView,
            handler = Handler(Looper.getMainLooper()),
            currentLocationBlock = {
                viewModel.addLocation(it)
            }
        )
    }

    private fun initData() {
        thread(true) {
            for (i in 1..3) {
                Thread.sleep(1000)
            }
            isReady = true
        }
    }

    private fun initSplashScreen() {
        initData()

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isReady) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSplashScreen()
        binding.viewModel = viewModel

        checkPermission()

        viewModel.currentLocationSignal.observe(this) { isCheck ->
            if (isCheck) {
                webViewBridge.getCurrentLocation()
                viewModel.offLocationSignal()
            }
        }

        binding.settingButton.setOnClickListener {
            showSettingDialog()
        }
    }

    private fun initWebView() {
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback?
            ) {
                super.onGeolocationPermissionsShowPrompt(origin, callback)
                callback?.invoke(origin, true, false)
            }
        }
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                showHistories()
            }
        }

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        binding.webView.addJavascriptInterface(webViewBridge, "Android")
        binding.webView.loadUrl("file:///android_asset/map.html")
    }

    private fun showHistories() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.locations.collect {
                    val locations = gson.toJson(it)
                    webViewBridge.showHistories(locations)
                }
            }
        }
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
            Toast.makeText(this, getString(R.string.locationError), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSettingDialog() {
        SettingDialog().show(supportFragmentManager, "SettingDialog")
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
