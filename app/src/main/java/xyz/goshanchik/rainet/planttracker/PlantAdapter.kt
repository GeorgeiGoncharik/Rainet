package xyz.goshanchik.rainet.planttracker

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.textclassifier.TextClassifier.TYPE_EMAIL
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.goshanchik.rainet.databinding.ListItemPlantBinding
import xyz.goshanchik.rainet.databinding.NoImageListItemPlantBinding
import xyz.goshanchik.rainet.model.Plant
import java.io.File
import java.lang.IllegalArgumentException


class PlantAdapter: ListAdapter<Plant, PlantAdapter.BaseViewHolder<*>>(PlantDiffUtil()) {

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    class ImageViewHolder private constructor(val binding: ListItemPlantBinding): BaseViewHolder<Plant?>(binding.root)
    {
        override fun bind(item: Plant?) {
            binding.plant = item
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ImageViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPlantBinding.inflate(layoutInflater, parent, false)
                return ImageViewHolder(binding = binding)
            }
        }
    }

    class NoImageViewHolder private constructor(val binding: NoImageListItemPlantBinding): BaseViewHolder<Plant?>(binding.root)
    {
        override fun bind(item: Plant?) {
            binding.plant = item
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): NoImageViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NoImageListItemPlantBinding.inflate(layoutInflater, parent, false)
                return NoImageViewHolder(binding = binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when(viewType){
            TYPE_NO_IMAGE -> NoImageViewHolder.from(parent)
            TYPE_IMAGE -> ImageViewHolder.from(parent)
            else -> throw IllegalArgumentException("No ViewHolder found.")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val item = getItem(position)
        when(holder){
            is NoImageViewHolder -> holder.bind(item as Plant)
            is ImageViewHolder -> holder.bind(item as Plant)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.photoUri.isEmpty()) {
            if(File(item.photoUri).exists())
                TYPE_IMAGE
            else
                TYPE_NO_IMAGE
        } else {
            TYPE_IMAGE
        }
    }

    fun getNoteAt(adapterPosition: Int): Plant {
        return getItem(adapterPosition)
    }

    companion object {
        private const val TYPE_NO_IMAGE = 0
        private const val TYPE_IMAGE = 1
    }
}