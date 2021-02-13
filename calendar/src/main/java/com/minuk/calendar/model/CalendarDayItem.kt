package com.minuk.calendar.model

data class CalendarDayItem(
    val year: Int,
    val month: Int,
    val day: Int,
    val isToday: Boolean = false,
    val isSunday: Boolean = false,
    val isCurrentMonth: Boolean = false
)