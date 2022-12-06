package com.example.bambino.track

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bambino.R
import com.example.bambino.database.TrackedAction

class TrackedActionAdapter : RecyclerView.Adapter<TrackedActionAdapter.ViewHolder>() {

    var data = listOf<TrackedAction>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val actionType: TextView = itemView.findViewById(R.id.action_type_string)
        private val actionTime: TextView = itemView.findViewById(R.id.action_time)
        private val actionIcon: ImageView = itemView.findViewById(R.id.action_type_icon)

        fun bind(item: TrackedAction) {
            actionType.text = item.actionType
            actionTime.text = item.actionTime.toString()
            actionIcon.setImageResource(
                when (item.actionType) {
                    "Bath" -> R.drawable.ic_baseline_bathtub_24
                    "Eat" -> R.drawable.ic_baseline_fastfood_24
                    "Diaper" -> R.drawable.ic_baseline_airline_seat_legroom_normal_24
                    "Sleep" -> R.drawable.ic_baseline_bedroom_child_24
                    else -> R.drawable.ic_baseline_cancel_24
                }
            )
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_tracked_action, parent, false)

                return ViewHolder(view)
            }
        }

    }

}



















