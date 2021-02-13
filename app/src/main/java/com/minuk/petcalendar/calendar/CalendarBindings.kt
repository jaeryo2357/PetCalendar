package com.minuk.petcalendar.calendar

import androidx.databinding.BindingAdapter
import com.minuk.calendar.PetCalendarView
import com.minuk.petcalendar.data.Date

@BindingAdapter("app:calendarDate")
fun setCalendarDate(calendar: PetCalendarView, calendarDate: Date) {
    calendar.setCalendarDate(
        calendarDate.year,
        calendarDate.monthOfYear,
        calendarDate.dayOfMonth
    )
}