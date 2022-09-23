package com.preonboarding.locationhistory.presentation.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.databinding.DialogHistoryBinding
import com.preonboarding.locationhistory.presentation.base.BaseDialog

class HistoryDialog : BaseDialog<DialogHistoryBinding>(R.layout.dialog_history) {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cancel.setOnClickListener { dismiss() }
        binding.confirm.setOnClickListener { dismiss() }
    }
}
