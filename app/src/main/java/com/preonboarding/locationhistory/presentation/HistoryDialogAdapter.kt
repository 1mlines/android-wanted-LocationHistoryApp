package com.preonboarding.locationhistory.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.preonboarding.locationhistory.data.History
import com.preonboarding.locationhistory.databinding.ItemHistoryBinding


class HistoryDialogAdapter :
    ListAdapter<History, HistoryDialogAdapter.HistoryDialogViewHolder>(DiffCallback) {

    private var hystoryList = emptyList<History>()

    fun setData(historylists: List<History>) {
        hystoryList = historylists
        notifyDataSetChanged()
    }

    class HistoryDialogViewHolder(private var binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(data: History) {
            binding.ItemNumTextView.text = data.num.toString()
            binding.ItemLatitudeTextView.text = data.latitude.toString()
            binding.ItemLongitudeTextView.text = data.longitude.toString()
            binding.ItemDateTextView.text = data.createdAt
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDialogViewHolder {

        return HistoryDialogViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryDialogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<History>() {


            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.num == newItem.num
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.num == newItem.num
            }
        }
    }


}