package com.preonboarding.locationhistory.presentation

import android.os.Handler
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.domain.model.Location
import com.preonboarding.locationhistory.util.JavaScripUrlUtil
import timber.log.Timber
import java.lang.ref.WeakReference

class WebViewBridge(
    private val gson: Gson,
    private val webView: WebView,
    private val handler: Handler,
    private val currentLocationBlock: (Location) -> Unit
) {

    @JavascriptInterface
    fun currentLocationCallback(location: String) {
        try {
            val data = gson.fromJson(location, Location::class.java)

            handler.post {
                currentLocationBlock(data)
            }
        } catch (e: Exception) {
            error(e.message)
        }
    }

    @JavascriptInterface
    fun getCurrentLocation() {
        val url = JavaScripUrlUtil.createMethodUrl("getCurrentLocation", null)

        handler.post {
            webView.loadUrl(url)
        }
    }

    @JavascriptInterface
    fun showHistories(locations: String) {
        val url = JavaScripUrlUtil.createMethodUrl("showHistories", locations)

        handler.post {
            webView.loadUrl(url)
        }
    }

    @JavascriptInterface
    fun error(message: String?) {
        val msg = webView.context.getString(R.string.mapError)
        Toast.makeText(webView.context, msg, Toast.LENGTH_SHORT).show()
        Timber.e(message)
    }
}