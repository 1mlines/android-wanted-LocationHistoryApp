package com.preonboarding.locationhistory.presentation.custom.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.databinding.FragmentTimerDialogBinding
import com.preonboarding.locationhistory.presentation.ui.main.MainViewModel
import com.preonboarding.locationhistory.presentation.uistates.DurationUiStates
import com.preonboarding.locationhistory.util.Alarm
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerFragmentDialog : DialogFragment() {
    private val binding by lazy { FragmentTimerDialogBinding.inflate(layoutInflater) }
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeDuration()
        setListener()
    }

    private fun setListener() = with(binding) {
        btOk.setOnClickListener {
            val duration = etTimer.text.toString()
            if (duration.toLong() != 0L) {
                viewModel.setCurrentSettingTime(duration = duration.toLong())
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.setting_timer_limit),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        btCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun observeDuration() = with(viewLifecycleOwner.lifecycleScope) {
        launch {
            repeatOnLifecycle(state = Lifecycle.State.RESUMED) {
                viewModel.currentSettingTime.collect { uiStates ->
                    when (uiStates) {
                        is DurationUiStates.Loading -> {
                        }
                        is DurationUiStates.Success -> {
                            binding.etTimer.setText(uiStates.duration.toString())
                        }
                        is DurationUiStates.DurationSaveSuccess -> {
                            val time: Long = binding.etTimer.text.toString().toLong()
                            Alarm.create(requireContext(), time)
                            delay(1_000)
                            dismiss()
                        }
                    }
                }
            }
        }
    }
}
