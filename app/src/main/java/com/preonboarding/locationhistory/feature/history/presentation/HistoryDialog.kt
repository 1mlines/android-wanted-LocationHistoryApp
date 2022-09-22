package com.preonboarding.locationhistory.feature.history.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.data.entity.History
import com.preonboarding.locationhistory.data.entity.toFormatDate
import com.preonboarding.locationhistory.databinding.DialogHistoryBinding
import com.preonboarding.locationhistory.feature.presentation.MainViewModel
import java.util.*

class HistoryDialog(
    private val viewModel: MainViewModel
) : DialogFragment() {
    private var _binding: DialogHistoryBinding? = null
    private val binding
        get() = _binding!!

    private val historyAdapter: HistoryListAdapter by lazy {
        HistoryListAdapter(
            itemClickListener = { doOnclick(it) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getHistoryFromDate(getCurrentDate())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_history, container, false)
        val view = binding.root
        observeToObservable()
        initView()
        return view
    }

    private fun initView() {
        binding.apply {
            tvHistoryDate.apply {
                setOnClickListener {
                    showDatePicker()
                }
                text = getCurrentDate()
            }
            rvHistoryList.adapter = historyAdapter
        }
    }

    private fun observeToObservable() {
        viewModel.historyFromDate.observe(this@HistoryDialog) { historyList ->
            historyAdapter.submitList(historyList?.toList())
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val date = "${year}." + formatMonth(month + 1) + ".${dayOfMonth}"
                binding.tvHistoryDate.text = date
                viewModel.getHistoryFromDate(date)
            }
        DatePickerDialog(
            requireActivity(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
        _binding = null
    }

    private fun getCurrentDate() =
        System.currentTimeMillis().toFormatDate()

    private fun formatMonth(month: Int): String {
        val formatMonth = "0$month"
        return if (formatMonth.length != 2) {
            formatMonth.substring(formatMonth.length - 2, formatMonth.length)
        } else {
            formatMonth
        }
    }

    private fun doOnclick(item: History) {
        //TODO 해당 좌표로 이동하는 로직 넣으면 좋을듯
    }
}