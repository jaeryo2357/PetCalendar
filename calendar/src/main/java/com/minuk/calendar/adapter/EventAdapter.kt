package com.minuk.calendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.minuk.calendar.data.Event
import com.minuk.calendar.databinding.ItemCalendarEventBinding
import com.minuk.calendar.viewholder.EventViewHolder
import kotlin.math.min

internal data class EventConfig(
    val maxEventCount: Int,
    val eventList: List<Event>,
)

internal class DayEventAdapter(
    private val eventConfig: EventConfig
) : RecyclerView.Adapter<EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {

        return EventViewHolder(
            ItemCalendarEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        if (position + 1 != eventConfig.maxEventCount) {
            holder.bindEvent(eventConfig.eventList[position])
        } else {
            holder.bindEventMore(eventConfig.eventList.size - eventConfig.maxEventCount + 1)
        }
    }

    override fun getItemCount(): Int {
        return min(
            eventConfig.eventList.size,
            eventConfig.maxEventCount
        )
    }
}