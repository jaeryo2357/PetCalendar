package com.minuk.petcalendar.calendar

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.minuk.petcalendar.base.BaseViewModel
import com.minuk.calendar.data.Date
import java.util.*

class CalendarViewModel @ViewModelInject constructor(

) : BaseViewModel() {

    private val _calendarDate = MutableLiveData(Date(Calendar.getInstance()))
    val calendarDate: LiveData<Date> = _calendarDate

    private val _currentMonthText = MutableLiveData<String>()
    val currentMonthText: LiveData<String> = _currentMonthText

    private val _currentMonthEvent = MutableLiveData<Boolean>()
    val currentMonthEvent: LiveData<Boolean> = _currentMonthEvent

    fun changeCalendarDate(calendarDate: Date) {
        _calendarDate.value = calendarDate
    }

    fun changeCurrentMonthString(currentMonthText: String) {
        _currentMonthText.value = currentMonthText
    }

    fun clickCurrentMonth() {
        _currentMonthEvent.value = true
    }
}