package com.minuk.calendar.model

import java.time.YearMonth

data class CalendarDayItem(
    val year: Int,
    val month: Int,
    val day: Int,
    val isToday: Boolean = false,
    val isWeekend: Boolean = false
)