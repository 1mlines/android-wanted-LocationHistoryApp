package com.preonboarding.locationhistory.ui.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.preonboarding.locationhistory.adapter.HistoryDialogAdapter
import com.preonboarding.locationhistory.databinding.DialogHistoryBinding
import com.preonboarding.locationhistory.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HistoryDialog : DialogFragment() {
    private lateinit var binding: DialogHistoryBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val mAdapter by lazy { HistoryDialogAdapter() }

    private var sizeX = 0
    private var sizeY = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogResize()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogHistoryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        makeHistoryRecyclerView()
        getTodayCalendar()
        initViewModel()

        return binding.root
    }

    private fun dialogResize() {
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = windowManager.currentWindowMetrics
            val insets =
                windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            sizeX = windowMetrics.bounds.width() - insets.left - insets.right
            sizeY = windowMetrics.bounds.height() - insets.bottom - insets.top
        } else {
            val display = windowManager.defaultDisplay
            val size = Point()
            display?.getSize(size)
            sizeX = size.x
            sizeY = size.y
        }
    }

    private fun showDateDialog() {
        val calendar = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(year, month, day)
            getSelectedCalendar(calendar.time)
            makeHistoryRecyclerView()
        }

        DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
        }.show()
    }

    private fun makeHistoryRecyclerView() {
        binding.recyclerViewHistoryDialog.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun getTodayCalendar() {
        val todayDate = System.currentTimeMillis()
        val datePattern = "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat(datePattern, Locale.getDefault()).format(todayDate)

        mainViewModel.changeDateName(dateFormat)
    }

    private fun getSelectedCalendar(date: Date) {
        val datePattern = "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat(datePattern, Locale.getDefault()).format(date)

        mainViewModel.changeDateName(dateFormat.format(date))
    }

    private fun initViewModel() {
        mainViewModel.dialogDatePicker.observe(this) {
            if (it.consumed) return@observe

            showDateDialog()
            it.consume()
        }

        mainViewModel.dialogConfirm.observe(this) {
            if (it.consumed) return@observe

            dialog?.dismiss()
            it.consume()
        }

        mainViewModel.dialogCancel.observe(this) {
            if (it.consumed) return@observe

            dialog?.dismiss()
            it.consume()
        }

        mainViewModel.historyResponse.observe(this) {
            mAdapter.setData(it)
        }
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
