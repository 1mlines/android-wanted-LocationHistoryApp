package com.preonboarding.locationhistory.presentation.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.preonboarding.locationhistory.databinding.ItemHistorydialogBinding
import com.preonboarding.locationhistory.domain.model.Location


class LocationAdapter : ListAdapter<Location, LocationAdapter.LocationHolder>(diffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHolder {
        val binding = ItemHistorydialogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationHolder, position: Int) {
        holder.bind(getItem(position))

    }

    inner class LocationHolder(private val binding: ItemHistorydialogBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Location) {
            binding.location = item
        }
    }

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem == newItem
            }


        }

    }

}
