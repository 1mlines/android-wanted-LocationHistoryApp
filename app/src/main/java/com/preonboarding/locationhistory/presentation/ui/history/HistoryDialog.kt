package com.preonboarding.locationhistory.presentation.ui.history

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.databinding.DialogHistoryBinding
import com.preonboarding.locationhistory.domain.model.Location
import com.preonboarding.locationhistory.presentation.base.BaseDialog
import com.preonboarding.locationhistory.presentation.ui.main.LocationAdapter
import com.preonboarding.locationhistory.presentation.ui.main.MainViewModel
import com.preonboarding.locationhistory.presentation.ui.util.currentDate
import com.preonboarding.locationhistory.presentation.ui.util.currentTimeStamp
import timber.log.Timber
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


class HistoryDialog : BaseDialog<DialogHistoryBinding>(R.layout.dialog_history) {

    private val locationAdapter by lazy {
        LocationAdapter()
    }

    private lateinit var callbacks: Callbacks

    interface Callbacks {
        fun getLocation(locations : List<Location>)
    }

    private val viewModel: MainViewModel by viewModels(ownerProducer = { requireActivity() })

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.historyRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            adapter = locationAdapter
        }

        val calendarStart = Calendar.getInstance()

        binding.dateButton.setOnClickListener {

            val today = Calendar.getInstance()
            val year = today.get(Calendar.YEAR)
            val month = today.get(Calendar.MONTH) + 1
            val day = today.get(Calendar.DAY_OF_MONTH)

            val dlg = DatePickerDialog(requireContext(),
                { _, pYear, pMonth, pDay ->
                    calendarStart.set(pYear,pMonth, pDay)

                    binding.dateButton.text = (
                            calendarStart.get(Calendar.YEAR).toString() // 왜 여기 toString() 해야함?
                                    + "/" + (calendarStart.get(Calendar.MONTH) + 1)
                                    + "/" + calendarStart.get(Calendar.DAY_OF_MONTH))

                },year, month, day)
            dlg.updateDate(calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH), calendarStart.get(Calendar.DAY_OF_MONTH)) // DatePicker 시작값 세팅
            dlg.show()

            val currentTime = System.currentTimeMillis()
            val dateFormatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
            val parseLongCurrentTime = dateFormatter.parse(dateFormatter.format(currentTime)).time

            Timber.d(parseLongCurrentTime.toString())

            viewModel.getLocations(parseLongCurrentTime)
            
            dlg.getButton(DatePickerDialog.BUTTON_NEGATIVE).setOnClickListener {
                dlg.dismiss()
            }

        }

    }

    override fun onResume() {
        super.onResume()
        val display = resources.displayMetrics

        val window: Window = dialog?.window ?: return
        val params: WindowManager.LayoutParams = window.attributes
        params.width = (display.widthPixels * 0.8).toInt() // 디바이스의 너비 80%
        params.height = (display.widthPixels * 1.2).toInt()

        dialog?.window?.attributes = params
    }
}
