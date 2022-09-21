package com.preonboarding.locationhistory.presentation

import android.os.Handler
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import com.google.gson.Gson
import com.preonboarding.locationhistory.domain.model.Location
import com.preonboarding.locationhistory.util.JavaScripUrlUtil
import timber.log.Timber
import java.lang.ref.WeakReference

class WebViewBridge(
    private val gson: Gson,
    private val webView: WebView,
    private val handler: WeakReference<Handler>,
    private val currentLocationBlock: (Location) -> Unit
) {

    @JavascriptInterface
    fun currentLocationCallback(location: String) {
        Timber.e("들어오나")
        try {
            val data = gson.fromJson(location, Location::class.java)

            handler.get()?.post {
                currentLocationBlock(data)
            }
        } catch (e: Exception) {
            error(e.message)
        }
    }

    @JavascriptInterface
    fun getCurrentLocation() {
        val url = JavaScripUrlUtil.createMethodUrl("getCurrentLocation", null)

        handler.get()?.post {
            webView.loadUrl(url)
        }
    }

    @JavascriptInterface
    fun showHistories(locations: String) {
        val url = JavaScripUrlUtil.createMethodUrl("showHistories", locations)

        handler.get()?.post {
            webView.loadUrl(url)
        }
    }

    @JavascriptInterface
    fun error(message: String?) {
        Toast.makeText(webView.context, "데이터를 불러오던 중 문제가 발생했습니다.", Toast.LENGTH_SHORT).show()
        Timber.e(message)
    }
}