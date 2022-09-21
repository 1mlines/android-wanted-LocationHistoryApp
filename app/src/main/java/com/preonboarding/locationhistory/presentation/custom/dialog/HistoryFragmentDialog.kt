package com.preonboarding.locationhistory.presentation.custom.dialog

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.preonboarding.locationhistory.databinding.FragmentHistoryDialogBinding
import com.preonboarding.locationhistory.presentation.ui.main.MainViewModel
import java.util.*


class HistoryFragmentDialog : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentHistoryDialogBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, com.preonboarding.locationhistory.R.style.BottomSheetDialog);
    }

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

        bindingViewModel()
        initListener()
    }

    private fun bindingViewModel() {
        lifecycleScope.launchWhenStarted {
            with(mainViewModel) {
                currentDate.collect {
                    binding.dialogDateTv.text = it

                    val dateInfo = it.split(".")
                    this.calendar.apply {
                        set(Calendar.YEAR, dateInfo[0].toInt())
                        set(Calendar.MONTH, dateInfo[1].toInt())
                        set(Calendar.DAY_OF_MONTH, dateInfo[2].toInt())
                    }
                }
            }
        }
    }

    private fun initTable() {
        // TODO : table 띄우기
    }

    private fun initListener() {
        binding.dialogDateTv.setOnClickListener {
            createDatePickerDialog()
        }

        binding.dialogCancelBtn.setOnClickListener {
            dismiss()
        }

        binding.dialogOkBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun createDatePickerDialog() {
        val calendar = mainViewModel.calendar

        val datePickerDialog = DatePickerDialog(
            this.requireContext(),
            { _, year, month, dayOfMonth ->
                mainViewModel.updateCurrentDate(year, month, dayOfMonth)
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