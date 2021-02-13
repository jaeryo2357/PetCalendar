package com.minuk.calendar.model

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

    override fun hashCode(): Int {
        var result = year
        result = 31 * result + month
        result = 31 * result + day
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other is Date) {
            return year == other.year &&
                    month == other.month &&
                    day == other.day
        }

        return false
    }
}