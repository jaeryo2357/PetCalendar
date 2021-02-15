package com.minuk.calendar.viewholder

import android.graphics.Color
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.minuk.calendar.R
import com.minuk.calendar.data.Event
import com.minuk.calendar.databinding.ItemCalendarEventBinding

internal class EventViewHolder(
    val binding: ItemCalendarEventBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bindEvent(event: Event) {
        binding.eventTextView.apply {
            text = event.eventText
            setTextColor(EVENT_TEXT_COLOR)
            background =
                ResourcesCompat.getDrawable(itemView.resources, R.drawable.bg_day_event, null)
        }
    }

    fun bindEventMore(remainEventSize: Int) {
        binding.eventTextView.apply {
            text = itemView.context.getString(R.string.event_more, remainEventSize)
            setTextColor(EVENT_MORE_TEXT_COLOR)
            background = null
        }
    }


    companion object {
        val EVENT_TEXT_COLOR = Color.WHITE

        val EVENT_MORE_TEXT_COLOR = Color.BLACK
    }
}