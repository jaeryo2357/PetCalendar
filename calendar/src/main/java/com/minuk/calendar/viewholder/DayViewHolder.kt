package com.minuk.calendar.viewholder

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView

import com.minuk.calendar.PetCalendarView
import com.minuk.calendar.R
import com.minuk.calendar.databinding.ItemCalendarMonthBinding
import com.minuk.calendar.model.Date

internal data class DayConfig(
    val dayWidth: Int,
    val dayHeight: Int,
    val calendarAccentColor: Int,
    val calendarNormalColor: Int
)

internal class PetCalenderViewHolder(
    private val binding: ItemCalendarMonthBinding,
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

    fun bindDayView(date: Date) {

        binding.dayCalendarTv.text = "${date.day}"

        //현재 요일의 경우 동그라미 이미지 표시
        if (date.isToday) {
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
            date.isToday -> Color.WHITE
            date.isSunday -> ContextCompat.getColor(itemView.context, R.color.calendar_sunday)
            else -> config.calendarNormalColor
        }

        if (date.isCurrentMonth) {
            itemView.alpha = 1.0f
        } else {
            itemView.alpha = 0.4f
        }

        binding.dayCalendarTv.setTextColor(textColor)

        itemView.setOnClickListener {
            eventHandler?.onDayClick(date.year, date.month, date.day)
        }
    }
}