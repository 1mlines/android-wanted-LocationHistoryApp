package com.preonboarding.locationhistory.feature.set

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.preonboarding.locationhistory.LocationHistoryApp
import com.preonboarding.locationhistory.databinding.DialogSetTimeBinding

class SetTimeDialog : DialogFragment() {
    private lateinit var binding: DialogSetTimeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DialogSetTimeBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView() {
        binding.apply {
            btnSetTimeCancel.setOnClickListener {
                dismiss()
            }
            btnSetTimePositive.setOnClickListener {
                LocationHistoryApp.prefs.setTime = editSetText.text.toString()
                dismiss()
            }
        }
    }
}