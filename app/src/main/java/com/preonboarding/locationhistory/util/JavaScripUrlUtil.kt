package com.preonboarding.locationhistory.util

import java.lang.StringBuilder

object JavaScripUrlUtil {
    private val sb = StringBuilder()

    fun createMethodUrl(methodName: String, json: String): String {
        sb.append("javascript:try {")
        sb.append("$methodName('$json');") //js 메소드 호출
        sb.append("}")
        sb.append("catch(exception) {")
        sb.append("Android.error(exception.message);") //bridge error 메소드 호출
        sb.append("}")

        val url = sb.toString()
        sb.clear()

        return url
    }
}