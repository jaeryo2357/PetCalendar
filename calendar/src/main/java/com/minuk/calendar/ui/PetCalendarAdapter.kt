package com.minuk.calendar.ui

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

import com.minuk.calendar.PetCalendarView
import com.minuk.calendar.databinding.ItemCalendarBinding
import com.minuk.calendar.model.CalendarDayItem

internal data class MonthConfig(
    val dayWidth: Int,
    val dayHeight: Int,
    val daysSize: Int,
    val year: Int,
    val today: Int,
    val currentMonth: Int,
    val startDayOfWeek: Int,
    val endDayOfLastMonth: Int,
    val endDayOfCurrentMonth: Int,
    val calendarAccentColor: Int,
    val calendarNormalColor: Int
)

internal class PetCalendarAdapter(
    var monthConfig: MonthConfig,
    var eventHandler: PetCalendarView.PetCalendarEventHandler?
) : RecyclerView.Adapter<PetCalenderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetCalenderViewHolder {

        val dayConfig = DayConfig(
            dayWidth = monthConfig.dayWidth,
            dayHeight = monthConfig.dayHeight,
            calendarAccentColor = monthConfig.calendarAccentColor,
            calendarNormalColor = monthConfig.calendarNormalColor
        )

        return PetCalenderViewHolder(
            ItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            dayConfig,
            eventHandler
        )
    }

    override fun onBindViewHolder(holder: PetCalenderViewHolder, position: Int) {

        val (month, day) = getMonthAndDayFromPosition(position)

        val isToday = position == monthConfig.today + monthConfig.startDayOfWeek - 1

        val isSunday = position % DAY_OF_WEEK == DAY_OF_SUNDAY

        val isCurrentMonth = monthConfig.currentMonth == month

        val calendarDayItem = CalendarDayItem(monthConfig.year, month, day,
            isToday,
            isSunday,
            isCurrentMonth
        )

        holder.bindDayView(calendarDayItem)
    }

    override fun getItemCount(): Int {
        return monthConfig.daysSize
    }

    /**
     * position 값으로 날짜 측정하는 함수
     * 한 달력 화면에서 최대 3개의 월 달력이 보여 월과 일을 구분
     *
     * @return month 와 day 를 Pair 객체로 반환합니다.
     */
    private fun getMonthAndDayFromPosition(position: Int): Pair<Int, Int> = when {
        position < monthConfig.startDayOfWeek -> monthConfig.currentMonth - 1 to
                (monthConfig.endDayOfLastMonth - (monthConfig.startDayOfWeek - position - 1))

        position > (monthConfig.endDayOfCurrentMonth + monthConfig.startDayOfWeek - 1) ->
            monthConfig.currentMonth + 1 to (position - (monthConfig.endDayOfCurrentMonth + monthConfig.startDayOfWeek) + 1)

        else ->
            monthConfig.currentMonth to (position - monthConfig.startDayOfWeek + 1)
    }

    companion object {
        const val DAY_OF_SUNDAY = 0
        const val DAY_OF_WEEK = 7
    }
}