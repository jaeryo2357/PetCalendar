package com.minuk.petcalendar.calendar

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.minuk.petcalendar.base.BaseViewModel
import com.minuk.petcalendar.data.Date
import java.util.*

class CalendarViewModel @ViewModelInject constructor(

) : BaseViewModel() {

    private val _calendarDate = MutableLiveData(Date(Calendar.getInstance()))
    val calendarDate: LiveData<Date> = _calendarDate

    private val _currentMonthString = MutableLiveData<String>()
    val currentMonthString: LiveData<String> = _currentMonthString

    private val _currentMonthEvent = MutableLiveData<Boolean>()
    val currentMonthEvent: LiveData<Boolean> = _currentMonthEvent

    fun changeCalendarDate(calendarDate: Date) {
        _calendarDate.value = calendarDate
    }

    fun changeCurrentMonthString(currentMonthString: String) {
        _currentMonthString.value = currentMonthString
    }

    fun clickCurrentMonth() {
        _currentMonthEvent.value = true
    }
}