package xyz.goshanchik.rainet.planttracker

import androidx.recyclerview.widget.DiffUtil
import xyz.goshanchik.rainet.model.Plant

class PlantDiffUtil: DiffUtil.ItemCallback<Plant>() {
    override fun areItemsTheSame(oldItem: Plant, newItem: Plant): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Plant, newItem: Plant): Boolean {
        return oldItem == newItem
    }
}