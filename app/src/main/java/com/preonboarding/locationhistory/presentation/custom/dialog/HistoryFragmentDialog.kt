package com.preonboarding.locationhistory.presentation.custom.dialog

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.databinding.FragmentHistoryDialogBinding
import com.preonboarding.locationhistory.presentation.ui.main.MainViewModel
import java.util.*


class HistoryFragmentDialog() : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentHistoryDialogBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupRatio(bottomSheetDialog)
        }
        return dialog
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

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams

        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 80 / 100
    }

    private fun getWindowHeight(): Int {
        val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = wm.currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.bottom - insets.top
        } else {
            val displayMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }

    companion object {
        const val TAG = "HistoryFragmentDialog"
    }
}