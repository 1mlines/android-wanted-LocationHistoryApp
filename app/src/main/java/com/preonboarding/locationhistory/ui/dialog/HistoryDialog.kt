package com.preonboarding.locationhistory.ui.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.preonboarding.locationhistory.databinding.DialogHistoryBinding
import com.preonboarding.locationhistory.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class HistoryDialog : DialogFragment() {
    private lateinit var binding: DialogHistoryBinding
    private lateinit var mainViewModel: MainViewModel

    private var sizeX = 0
    private var sizeY = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        dialogResize()
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogHistoryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        clickListeners()
        getTodayCalendar()
        return binding.root
    }

    private fun clickListeners() {
        binding.textViewHistoryDialogDate.setOnClickListener {
            showDateDialog()
        }

        binding.buttonHistoryDialogCancel.setOnClickListener {
            dialog?.dismiss()
        }

        binding.buttonHistoryDialogSubmit.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun dialogResize() {
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        val display = windowManager?.defaultDisplay
        val size = Point()
        display?.getSize(size)
        sizeX = size.x
        sizeY = size.y
    }

    private fun showDateDialog() {
        val cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            cal.set(year, month, day)
            getSelectedCalendar(cal.time)
        }

        DatePickerDialog(
            requireContext(),
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH),
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
        }.show()
    }

    private fun getTodayCalendar() {
        val todayDate = System.currentTimeMillis()
        val datePattern = "yyyy.MM.dd"
        val dateFormat = SimpleDateFormat(datePattern, Locale.getDefault()).format(todayDate)

        mainViewModel.changeDateName(dateFormat)
    }

    private fun getSelectedCalendar(date: Date) {
        val datePattern = "yyyy.MM.dd"
        val dateFormat = SimpleDateFormat(datePattern, Locale.getDefault()).format(date)

        mainViewModel.changeDateName(dateFormat.format(date))
    }

    override fun onResume() {
        super.onResume()

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = sizeX
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as? WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}