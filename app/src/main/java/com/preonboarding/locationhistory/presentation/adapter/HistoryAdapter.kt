package com.preonboarding.locationhistory.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.preonboarding.locationhistory.databinding.ItemHistoryBinding
import com.preonboarding.locationhistory.domain.model.Location
import com.preonboarding.locationhistory.presentation.ui.util.convertTimeStampToDateWithTime

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val histories = arrayListOf<Location>()

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Location) {
            binding.date.text = item.date.convertTimeStampToDateWithTime()
            binding.id.text = item.id.toString()
            binding.latitude.text = item.latitude.toString()
            binding.longitude.text = item.longitude.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) =
        holder.bind(histories[position])

    override fun getItemCount(): Int = histories.size

    fun submitList(items: List<Location>) {
        histories.clear()
        histories.addAll(items)
        notifyDataSetChanged()
    }
}
