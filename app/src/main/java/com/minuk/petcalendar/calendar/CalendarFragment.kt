package com.minuk.petcalendar.calendar

import com.minuk.petcalendar.R
import com.minuk.petcalendar.base.BaseFragment
import com.minuk.petcalendar.databinding.FragmentCalendarBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {

    companion object {
        fun newInstance() = CalendarFragment()
    }
}