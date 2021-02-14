package com.minuk.calendar.data

data class Event(
    val eventText: String,
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val hour: Int = 0,
    val minute: Int = 0,
)