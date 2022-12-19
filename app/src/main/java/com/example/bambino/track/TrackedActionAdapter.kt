package com.example.bambino.track

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ExpandableListView.OnChildClickListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bambino.R
import com.example.bambino.database.TrackedAction
import com.example.bambino.databinding.ListItemTrackedActionBinding
import java.text.SimpleDateFormat
import java.util.*

class TrackedActionAdapter(private val clickListener: TrackedActionListener) :
    ListAdapter<TrackedAction, TrackedActionAdapter.ViewHolder>(TrackedActionDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class ViewHolder private constructor(private val binding: ListItemTrackedActionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TrackedAction, clickListener: TrackedActionListener) {
            binding.actionTypeString.text = "Activity: ${item.actionType}"
            binding.actionTime.text = SimpleDateFormat("HH:mm", Locale.UK)
                .format(item.actionTime).toString()
            binding.actionTypeIcon.setImageResource(
                when (item.actionType) {
                    "Bath" -> R.drawable.ic_baseline_bathtub_48
                    "Eat" -> R.drawable.ic_baseline_fastfood_48
                    "Diaper" -> R.drawable.ic_baseline_airline_seat_legroom_normal_48
                    "Sleep" -> R.drawable.ic_baseline_bedroom_child_48
                    else -> R.drawable.ic_baseline_cancel_24
                }
            )
            binding.actionHumour.text = "Humour: ${item.actionHumour}"

            binding.action = item
            binding.clickListener = clickListener

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTrackedActionBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    class TrackedActionDiffCallback : DiffUtil.ItemCallback<TrackedAction>() {
        override fun areItemsTheSame(oldItem: TrackedAction, newItem: TrackedAction): Boolean {
            return oldItem.actionId == newItem.actionId
        }

        override fun areContentsTheSame(oldItem: TrackedAction, newItem: TrackedAction): Boolean {
            return oldItem == newItem
        }

    }

    class TrackedActionListener(val clickListener: (actionId: Long) -> Unit) {
        fun onClick(action: TrackedAction) = clickListener(action.actionId)
    }

}



















