package com.example.bambino.memories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bambino.database.Memory
import com.example.bambino.databinding.ListItemMemoryBinding
import java.text.SimpleDateFormat
import java.util.*

class MemoryAdapter(private val clickListener: MemoryListener) :
    ListAdapter<Memory, MemoryAdapter.ViewHolder>(MemoryDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }


    class ViewHolder private constructor(val binding: ListItemMemoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Memory, clickListener: MemoryListener) {

            binding.memory = item
            binding.clickListener = clickListener

            Glide
                .with(itemView.context)
                .load(item.memoryPhotoUri)
                .centerCrop()
                .into(binding.memoryImageElement)

            binding.memoryDate.text = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.UK)
                .format(item.memoryDate).toString()

            binding.memoryDescription.text = item.memoryDescription
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListItemMemoryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class MemoryDiffCallback : DiffUtil.ItemCallback<Memory>() {
        override fun areItemsTheSame(oldItem: Memory, newItem: Memory): Boolean {
            return oldItem.memoryId == newItem.memoryId
        }

        override fun areContentsTheSame(oldItem: Memory, newItem: Memory): Boolean {
            return oldItem == newItem
        }

    }

    class MemoryListener(val clickListener: (memoryId: Long) -> Unit) {
        fun onClick(memory: Memory) = clickListener(memory.memoryId)
    }
}

