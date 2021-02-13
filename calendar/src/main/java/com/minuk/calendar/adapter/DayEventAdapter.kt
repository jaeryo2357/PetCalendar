package com.minuk.calendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.minuk.calendar.databinding.ItemCalendarDayBinding
import com.minuk.calendar.model.Event
import com.minuk.calendar.viewholder.DayEventViewHolder

internal data class EventConfig(
    val maxEventCount: Int,
    val eventList: List<Event>,
)

internal class DayEventAdapter(
    private val eventConfig: EventConfig
) : RecyclerView.Adapter<DayEventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayEventViewHolder {

        return DayEventViewHolder(
            ItemCalendarDayBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: DayEventViewHolder, position: Int) {
        holder.bind(eventConfig.eventList[position])
    }

    override fun getItemCount(): Int {
        return eventConfig.maxEventCount
    }
}