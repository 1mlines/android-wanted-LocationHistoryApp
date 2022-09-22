package com.preonboarding.locationhistory.presentation.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.databinding.DialogSettingBinding
import com.preonboarding.locationhistory.presentation.base.BaseDialog
import com.preonboarding.locationhistory.presentation.ui.worker.CurrentLocationWorker
import java.util.concurrent.TimeUnit


class SettingDialog : BaseDialog<DialogSettingBinding>(R.layout.dialog_setting) {

    private lateinit var viewModel: SettingViewModel
    private lateinit var workManager: WorkManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SettingViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getStorageInterval()

        viewModel.storageInterval.observe(viewLifecycleOwner) {
            binding.etMinute.setText(it.toString())
        }

        binding.cancel.setOnClickListener { dismiss() }

        binding.confirm.setOnClickListener {
            val interval = binding.etMinute.text.toString().toLong()
            viewModel.updateStorageInterval(interval)
            dismiss()
        }
    }
}
