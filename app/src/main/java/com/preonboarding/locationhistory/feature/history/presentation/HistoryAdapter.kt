package com.preonboarding.locationhistory.feature.history.presentation

import android.widget.TextView
import androidx.databinding.BindingAdapter

object HistoryAdapter {

    @JvmStatic
    @BindingAdapter("bindLocation")
    fun TextView.bindLocation(dot: Double) {
        text = String.format("%.1f", dot)
    }
}