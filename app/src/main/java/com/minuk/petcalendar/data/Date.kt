package com.minuk.petcalendar.data

import java.util.*

data class Date(val year: Int, val monthOfYear: Int, val dayOfMonth: Int) {

    constructor(calendar: Calendar) : this(
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )
}
