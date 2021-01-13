package com.minuk.petcalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.minuk.calendar.PetCalendarView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var calendar: PetCalendarView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendar = findViewById(R.id.petcalendar)

        findViewById<Button>(R.id.change_calendar_btn).setOnClickListener {
            val testCalendar = Calendar.getInstance()
            testCalendar.add(Calendar.MONTH, 1)

            calendar.setCalendarDate(testCalendar)
        }
    }
}