package com.preonboarding.locationhistory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.preonboarding.locationhistory.databinding.ItemRowHistoryBinding
import com.preonboarding.locationhistory.local.entity.History
import com.preonboarding.locationhistory.local.util.DiffUtils

class HistoryDialogAdapter(
) : RecyclerView.Adapter<HistoryDialogAdapter.HistoryDialogViewHolder>() {

    private var historyList: List<History> = emptyList()

    class HistoryDialogViewHolder(private val binding: ItemRowHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            binding.history = history
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): HistoryDialogViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRowHistoryBinding.inflate(layoutInflater, parent, false)
                return HistoryDialogViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDialogViewHolder {
        return HistoryDialogViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HistoryDialogViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    fun setData(newData: List<History>) {
        val historyDiffUtil = DiffUtils(historyList, newData)
        val diffUtilResult = DiffUtil.calculateDiff(historyDiffUtil)

        historyList = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}