package com.preonboarding.locationhistory.presentation.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.databinding.DialogSettingBinding
import com.preonboarding.locationhistory.presentation.base.BaseDialog
import com.preonboarding.locationhistory.presentation.ui.main.MainViewModel

class SettingDialog : BaseDialog<DialogSettingBinding>(R.layout.dialog_setting) {

    private val viewModel: MainViewModel by viewModels(ownerProducer = { requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }
}
