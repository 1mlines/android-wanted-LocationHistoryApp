package com.preonboarding.locationhistory.presentation

import android.os.Handler
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.preonboarding.locationhistory.util.JsCallbackUtil
import java.lang.ref.WeakReference

class WebViewBridge(
    private val webView: WebView,
    private val mHandler: WeakReference<Handler>
) {

    @JavascriptInterface
    fun getCurrentLocation(location: String) {

    }

    @JavascriptInterface
    fun showHistories(locations: String) {
        val url = JsCallbackUtil.createMethodUrl("getLocations", locations)

        mHandler.get()?.post {
            webView.loadUrl(url)
        }
    }

}