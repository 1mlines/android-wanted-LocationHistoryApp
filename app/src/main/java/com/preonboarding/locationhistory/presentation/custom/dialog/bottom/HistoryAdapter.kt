package com.preonboarding.locationhistory.presentation.custom.dialog.bottom

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.preonboarding.locationhistory.databinding.ItemHistoryListBinding
import com.preonboarding.locationhistory.presentation.model.Location

class HistoryAdapter(private val locations: MutableList<Location>)
    :RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateHistoryList(curLocations : MutableList<Location>) {
        locations.clear()
        locations.addAll(curLocations)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemHistoryListBinding = ItemHistoryListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(locations[position])
    }

    override fun getItemCount(): Int  = locations.size

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

}