package com.minuk.calendar.viewholder

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.minuk.calendar.PetCalendarView
import com.minuk.calendar.R
import com.minuk.calendar.adapter.DayEventAdapter
import com.minuk.calendar.adapter.EventConfig
import com.minuk.calendar.databinding.ItemCalendarDayBinding
import com.minuk.calendar.data.Date
import com.minuk.calendar.data.Event

internal data class DayConfig(
    val dayWidth: Int,
    val dayHeight: Int,
    val calendarAccentColor: Int,
    val calendarNormalColor: Int
)

internal class PetCalenderViewHolder(
    private val binding: ItemCalendarDayBinding,
    private val config: DayConfig,
    private val eventHandler: PetCalendarView.PetCalendarEventHandler?
) : RecyclerView.ViewHolder(binding.root) {

    init {
        initLayout()
    }

    private fun initLayout() {
        val layoutParams = binding.root.layoutParams.apply {
            width = config.dayWidth
            height = config.dayHeight
        }

        binding.root.layoutParams = layoutParams
    }

    fun bindDate(date: Date) {

        binding.dayTextView.text = "${date.day}"

        //현재 요일의 경우 동그라미 이미지 표시
        if (date.isToday) {
            val drawable = ResourcesCompat.getDrawable(
                itemView.resources,
                R.drawable.bg_date_selected, null
            )?.apply {
                DrawableCompat.setTint(this, config.calendarAccentColor)
            }

            binding.dayTextView.background = drawable
        } else {
            binding.dayTextView.background = null
        }

        val textColor = when {
            date.isToday -> Color.WHITE
            date.isSunday -> ContextCompat.getColor(itemView.context, R.color.calendar_sunday)
            else -> config.calendarNormalColor
        }

        binding.dayLayout.alpha = if (date.isCurrentMonth) 1.0f else 0.4f

        binding.dayTextView.setTextColor(textColor)

        itemView.setOnClickListener {
            eventHandler?.onDayClick(date.year, date.month, date.day)
        }
    }

    fun bindEvent(maxEventCount: Int, eventList: List<Event>) {

        val eventConfig = EventConfig(maxEventCount, eventList)

        val adapter = DayEventAdapter(eventConfig)

        binding.dayEventRecyclerView.apply {
            layoutManager = LinearLayoutManager(itemView.context)
            this.adapter = adapter
        }
    }
}