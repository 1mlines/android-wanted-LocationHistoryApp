package com.preonboarding.locationhistory.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.preonboarding.locationhistory.data.HistoryRepository
import com.preonboarding.locationhistory.data.HistoryRepositoryImpl


class HistoryDialogViewModelFactory(private val repository: HistoryRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return HistoryDialogViewModel(repository) as T

    }

}