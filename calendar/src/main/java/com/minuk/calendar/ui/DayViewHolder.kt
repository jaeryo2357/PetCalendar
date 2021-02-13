package com.minuk.calendar.ui

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView

import com.minuk.calendar.PetCalendarView
import com.minuk.calendar.R
import com.minuk.calendar.databinding.ItemCalendarBinding
import com.minuk.calendar.model.CalendarDayItem

internal data class DayConfig(
    val dayWidth: Int,
    val dayHeight: Int,
    val calendarAccentColor: Int,
    val calendarNormalColor: Int
)

internal class PetCalenderViewHolder(
    private val binding: ItemCalendarBinding,
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
        // Log.d(TAG, itemViewRatio)

        binding.root.layoutParams = layoutParams
    }

    fun bindDayView(dayItem: CalendarDayItem) {

        binding.dayCalendarTv.text = "${dayItem.day}"

        //현재 요일의 경우 동그라미 이미지 표시
        if (dayItem.isToday) {
            val drawable = ResourcesCompat.getDrawable(
                itemView.resources,
                R.drawable.bg_date_selected, null
            )?.apply {
                DrawableCompat.setTint(this, config.calendarAccentColor)
            }

            binding.dayCalendarTv.background = drawable
        } else {
            binding.dayCalendarTv.background = null
        }

        val textColor = when {
            dayItem.isToday -> Color.WHITE
            dayItem.isSunday -> ContextCompat.getColor(itemView.context, R.color.calendar_sunday)
            else -> config.calendarNormalColor
        }

        if (dayItem.isCurrentMonth) {
            itemView.alpha = 1.0f
        } else {
            itemView.alpha = 0.4f
        }

        binding.dayCalendarTv.setTextColor(textColor)

        itemView.setOnClickListener {
            eventHandler?.onDayClick(dayItem.year, dayItem.month, dayItem.day)
        }
    }
}