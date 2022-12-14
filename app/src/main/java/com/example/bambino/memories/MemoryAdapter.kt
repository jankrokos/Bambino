package com.example.bambino.memories

import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bambino.R
import com.example.bambino.database.Memory
import com.example.bambino.track.TrackedActionAdapter
import java.text.SimpleDateFormat
import java.util.*

class MemoryAdapter(val fragment: Fragment) :
    RecyclerView.Adapter<MemoryAdapter.ViewHolder>() {


    var data = listOf<Memory>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        holder.memoryDate.text = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.UK)
            .format(item.memoryDate).toString()

        holder.memoryDescription.text = item.memoryDescription

        Log.i("adapter", "${item.memoryPhotoUri} - ${Uri.parse(item.memoryPhotoUri).path}")

        Glide
            .with(fragment)
            .load(item.memoryPhotoUri)
            .centerCrop()
            .into(holder.memoryPhoto)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_memory, parent, false)
        return ViewHolder(view)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val memoryPhoto: ImageView = itemView.findViewById(R.id.memory_image_element)
        val memoryDate: TextView = itemView.findViewById(R.id.memory_date)
        val memoryDescription: TextView = itemView.findViewById(R.id.memory_description)
    }


//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = data[position]
//        holder.bind(item)
//    }
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): ViewHolder {
//        return ViewHolder.from(parent)
//    }
//
//    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val memoryPhoto: ImageView = itemView.findViewById(R.id.memory_image_element)
//        private val memoryDate: TextView = itemView.findViewById(R.id.memory_date)
//        private val memoryDescription: TextView = itemView.findViewById(R.id.memory_description)
//
//
//
//
//        fun bind(item: Memory) {
//            Glide
//                .with()
//                .load(item.memoryPhotoUri)
//                .centerCrop()
//                .into(memoryPhoto)
//
//            memoryDate.text = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.UK)
//                .format(item.memoryDate).toString()
//
//            memoryDescription.text = item.memoryDescription
//        }
//
//        companion object {
//            fun from(parent: ViewGroup): ViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val view = layoutInflater.inflate(R.layout.list_item_memory, parent, false)
//                return ViewHolder(view)
//            }
//        }
//
//    }
}
