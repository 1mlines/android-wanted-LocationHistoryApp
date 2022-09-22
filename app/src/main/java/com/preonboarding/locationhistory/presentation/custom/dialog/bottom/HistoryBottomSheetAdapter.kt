package com.preonboarding.locationhistory.presentation.custom.dialog.bottom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.preonboarding.locationhistory.databinding.ItemHistoryListBinding
import com.preonboarding.locationhistory.presentation.model.Location
import timber.log.Timber

class HistoryBottomSheetAdapter
    : ListAdapter<Location, HistoryBottomSheetAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(location = it)
        }
    }

    inner class ViewHolder(private val binding: ItemHistoryListBinding)
        :RecyclerView.ViewHolder(binding.root) {
        fun bind(location: Location) {
            binding.apply {
                itemHistoryNumberTv.text = "%d".format(adapterPosition + 1)
                itemHistoryLatitudeTv.text = location.latitude.toString()
                itemHistoryLongitudeTv.text = location.longitude.toString()
                itemHistoryDateTv.text = location.date.toString()
            }
        }
    }

    companion object {
        private const val TAG = "HistoryBottomSheetAdapter"

        private val diffCallback = object : DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
                Timber.tag(TAG).e("oldid : ${oldItem.id} newid : ${newItem.id} return ${oldItem.id == newItem.id}")
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
                Timber.tag(TAG).e("olditem : $oldItem newitem : $newItem return ${oldItem == newItem}")
                return oldItem == newItem
            }
        }
    }
}