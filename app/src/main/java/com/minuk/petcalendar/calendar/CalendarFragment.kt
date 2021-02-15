package com.minuk.petcalendar.calendar

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.minuk.petcalendar.R
import com.minuk.petcalendar.base.BaseFragment
import com.minuk.petcalendar.databinding.FragmentCalendarBinding

import com.minuk.calendar.data.Date

import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {

    private val calendarViewModel: CalendarViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            viewModel = calendarViewModel
            lifecycleOwner = lifecycleOwner
        }

        observeViewModel()

        initViewModel()
    }

    private fun observeViewModel() {
        calendarViewModel.currentMonthEvent.observe(viewLifecycleOwner) { event ->
            if (event) {

                val date = calendarViewModel.calendarDate.value
                date?.let {
                    showDatePickerDialog(it)
                }
            }
        }
    }

    private fun initViewModel() {
        val currentMonthString = getCurrentMonthString(Date(Calendar.getInstance()))

        calendarViewModel.changeCurrentMonthString(currentMonthString)
    }

    private val datePickerListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val calendarDate = Date(year, month, dayOfMonth)

            calendarViewModel.changeCalendarDate(calendarDate)
            calendarViewModel.changeCurrentMonthString(getCurrentMonthString(calendarDate))
        }


    private fun getCurrentMonthString(calendarDate: Date) =
        requireContext().getString(
            R.string.format_calendar_date,
            calendarDate.year, calendarDate.month + 1
        )

    private fun showDatePickerDialog(date: Date) {
        val dialog = DatePickerDialog(
            requireContext(),
            datePickerListener,
            date.year, date.month, date.day
        )

        dialog.show()
    }


    companion object {
        fun newInstance() = CalendarFragment()
    }
}