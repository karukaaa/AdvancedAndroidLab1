package com.example.lab1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.lab1.databinding.CalendarEventLayoutBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalendarEventAdapter :
    ListAdapter<CalendarEvent, CalendarEventAdapter.CalendarEventViewHolder>(
        CalendarEventDiffCallback()
    ) {
    var eventList: List<CalendarEvent> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarEventViewHolder {
        return CalendarEventViewHolder(
            CalendarEventLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CalendarEventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CalendarEventViewHolder(
        private val binding: CalendarEventLayoutBinding
    ) : ViewHolder(binding.root) {

        fun bind(event: CalendarEvent) {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
            val formattedDate = dateFormat.format(Date(event.startTime))

            binding.eventName.text = event.title
            binding.eventStartTime.text = formattedDate
        }
    }
}

class CalendarEventDiffCallback : DiffUtil.ItemCallback<CalendarEvent>() {
    override fun areItemsTheSame(oldItem: CalendarEvent, newItem: CalendarEvent): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CalendarEvent, newItem: CalendarEvent): Boolean {
        return oldItem == newItem
    }

}
