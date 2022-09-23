package com.preonboarding.locationhistory.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.databinding.DialogSettingBinding
import com.preonboarding.locationhistory.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingDialog : DialogFragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: DialogSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DialogSettingBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        clickEvent()

        return binding.root
    }

    private fun clickEvent() {
        binding.apply {
            buttonSettingSubmit.setOnClickListener {
                val minuteText = editTextSettingMinute.text.toString()

                when (minuteText.length) {
                    0 -> Toast.makeText(
                        context,
                        getString(R.string.setting_toast_empty_minute_message),
                        Toast.LENGTH_LONG
                    ).show()
                    else -> {
                        val minute = minuteText.toLong()

                        if (minute >= 1) {
                            viewModel?.changeSaveInterval(minute)
                            dialog?.dismiss()
                        } else {
                            Toast.makeText(
                                context,
                                getString(R.string.setting_taost_zero_minute_message),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
            buttonSettingCancel.setOnClickListener {
                dialog?.dismiss()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setUpDialogFragment()
    }

    private fun setUpDialogFragment() {
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as? WindowManager

        if (Build.VERSION.SDK_INT < 30) {
            val display = windowManager?.defaultDisplay
            val size = Point()

            display?.getSize(size)

            val width = (size.x * 0.8).toInt()
            val height = LayoutParams.WRAP_CONTENT
            val window = dialog?.window

            window?.setLayout(width, height)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        } else {
            val rect = windowManager?.currentWindowMetrics?.bounds
            val window = dialog?.window

            if (rect != null) {
                val width = (rect.width() * 0.8).toInt()
                val height = LayoutParams.WRAP_CONTENT
                window?.setLayout(width, height)
            }
        }
    }
}