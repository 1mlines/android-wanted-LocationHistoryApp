package com.preonboarding.locationhistory.presentation.custom.dialog

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.preonboarding.locationhistory.databinding.FragmentHistoryDialogBinding
import com.preonboarding.locationhistory.presentation.ui.main.MainViewModel
import timber.log.Timber
import java.util.*

class HistoryFragmentDialog : DialogFragment() {
    private lateinit var binding: FragmentHistoryDialogBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = false // 밖에 눌러도 dismiss 되지 않음
    }


    // TODO : dialog 크기 논의
    override fun onResume() {
        super.onResume()

        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
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
            mainViewModel.currentDate.collect {
                binding.dialogDateTv.text = it
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
        val currentDate = mainViewModel.currentDate.value

        val calendar = Calendar.getInstance().apply {
            val dateInfo = currentDate.split(".")
            set(Calendar.YEAR, dateInfo[0].toInt())
            set(Calendar.MONTH, dateInfo[1].toInt())
            set(Calendar.DAY_OF_MONTH, dateInfo[2].toInt())
        }

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