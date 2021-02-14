package com.minuk.petcalendar.calendar

import androidx.databinding.BindingAdapter
import com.minuk.calendar.PetCalendarView
import com.minuk.calendar.data.Date

@BindingAdapter("app:calendarDate")
fun setCalendarDate(calendar: PetCalendarView, calendarDate: Date) {
    calendar.setCalendarDate(
        calendarDate.year,
        calendarDate.month,
        calendarDate.day
    )
}