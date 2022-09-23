package com.preonboarding.locationhistory.presentation.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.preonboarding.locationhistory.R
import com.preonboarding.locationhistory.databinding.DialogHistoryBinding
import com.preonboarding.locationhistory.presentation.adapter.HistoryAdapter
import com.preonboarding.locationhistory.presentation.base.BaseDialog
import com.preonboarding.locationhistory.presentation.ui.main.MainViewModel
import com.preonboarding.locationhistory.presentation.ui.util.convertTimeStampToDate
import kotlinx.coroutines.launch

class HistoryDialog : BaseDialog<DialogHistoryBinding>(R.layout.dialog_history) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: HistoryAdapter

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

        setAdapter()
        showHistories()

        binding.cancel.setOnClickListener { dismiss() }
        binding.confirm.setOnClickListener { dismiss() }
    }

    private fun setAdapter() {
        adapter = HistoryAdapter()

        binding.rcv.apply {
            adapter = this@HistoryDialog.adapter
            this.layoutManager = LinearLayoutManager(requireActivity())
            this.setHasFixedSize(true)
        }
    }

    private fun showHistories() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.locations.collect { locations ->
                    adapter.submitList(locations)
                }
            }
        }
    }
}
