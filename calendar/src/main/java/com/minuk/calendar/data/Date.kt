package com.minuk.calendar.data

import java.util.*

data class Date(
    val year: Int,
    val month: Int,
    val day: Int,
    val isToday: Boolean = false,
    val isSunday: Boolean = false,
    val isCurrentMonth: Boolean = false
) {

    constructor(calendar: Calendar) : this(
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )
}