package com.preonboarding.locationhistory.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter

object HistoryDialogBindingAdapter {
    @BindingAdapter("setNumber")
    @JvmStatic
    fun setNumber(textView: TextView, id: Long) {
        textView.text = id.toString()
    }

    @BindingAdapter("setLatitude")
    @JvmStatic
    fun setLatitude(textView: TextView, latitude: Double) {
        textView.text = latitude.toString()
    }

    @BindingAdapter("setLongitude")
    @JvmStatic
    fun setLongitude(textView: TextView, longitude: Double) {
        textView.text = longitude.toString()
    }

    @BindingAdapter("setDate")
    @JvmStatic
    fun setDate(textView: TextView, date: String) {
        textView.text = date
    }
}