package com.preonboarding.locationhistory.presentation

import android.webkit.JavascriptInterface
import com.google.gson.Gson
import com.preonboarding.locationhistory.data.di.MainDispatcher
import com.preonboarding.locationhistory.domain.model.Location
import com.preonboarding.locationhistory.presentation.ui.main.LoadUrlCallbackInterface
import com.preonboarding.locationhistory.presentation.ui.main.LocationCallbackInterface
import com.preonboarding.locationhistory.presentation.ui.main.ShowMessageCallbackInterface
import com.preonboarding.locationhistory.util.JavaScripUrlUtil
import kotlinx.coroutines.*
import javax.inject.Inject

class WebViewBridge @Inject constructor(
    private val gson: Gson,
    @MainDispatcher mainDispatcher: CoroutineDispatcher,
    private val locationCallback: LocationCallbackInterface?,
    private val loadUrlCallback: LoadUrlCallbackInterface?,
    private val showMsgCallback: ShowMessageCallbackInterface?
) {

    private val superJob =
        SupervisorJob() + mainDispatcher + CoroutineExceptionHandler { _, throwable ->
            error(throwable.stackTraceToString())
        }

    @JavascriptInterface
    fun currentLocationCallback(json: String) {
        CoroutineScope(superJob).launch {
            val location = gson.fromJson(json, Location::class.java)
            locationCallback?.getCurrentLocation(location)
        }
    }

    @JavascriptInterface
    fun getCurrentLocation() {
        CoroutineScope(superJob).launch {
            val url = JavaScripUrlUtil.createMethodUrl("getCurrentLocation", null)
            loadUrlCallback?.loadUrl(url)
        }
    }

    @JavascriptInterface
    fun showHistories(locations: List<Location>) {
        CoroutineScope(superJob).launch {
            val json = gson.toJson(locations)
            val url = JavaScripUrlUtil.createMethodUrl("showHistories", json)

            loadUrlCallback?.loadUrl(url)
        }
    }

    @JavascriptInterface
    fun error(message: String) {
        showMsgCallback?.error(message)
    }
}