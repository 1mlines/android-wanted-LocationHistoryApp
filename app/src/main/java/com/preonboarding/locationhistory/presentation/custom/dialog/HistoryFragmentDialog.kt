package com.preonboarding.locationhistory.presentation.custom.dialog

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.databinding.FragmentHistoryDialogBinding
import java.text.SimpleDateFormat
import java.util.*

class HistoryFragmentDialog : DialogFragment() {
    private lateinit var binding: FragmentHistoryDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentHistoryDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO : table 띄우기
        initListener()
    }

    private fun initListener() {
        binding.dialogDateTv.setOnClickListener {
            createDatePickerDialog()
        }

        binding.dialogCancelBtn.setOnClickListener {
            dismiss()
        }

        binding.dialogOkBtn.setOnClickListener {
            // TODO : 선택 된 날짜의 history 만 맵에 띄우도록 갱신
            dismiss()
        }
    }

    private fun createDatePickerDialog() {
        // TODO : current date 정보 필요
        val currentDate = "2022.09.21"
        val datePattern = "yyyy.MM.dd"

        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, currentDate.split("-")[0].toInt())
            set(Calendar.MONTH, currentDate.split("-")[1].toInt())
            set(Calendar.DAY_OF_MONTH, currentDate.split("-")[2].toInt())
        }

        val datePickerDialog = DatePickerDialog(
            this.requireContext(),
            { _, year, month, dayOfMonth ->

                // TODO : current date 정보 갱신
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                binding.dialogDateTv.text =
                    SimpleDateFormat(datePattern, Locale.getDefault()).format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) - 1,
            calendar.get(Calendar.DAY_OF_MONTH),
        )
        datePickerDialog.show()

    }

    companion object {
        const val TAG = "HistoryFragmentDialog"
    }


}