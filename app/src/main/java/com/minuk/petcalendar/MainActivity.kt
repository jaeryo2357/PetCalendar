package com.minuk.petcalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.minuk.calendar.PetCalendarView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val calendar: PetCalendarView by lazy { findViewById(R.id.petcalendar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val testCalendar = Calendar.getInstance()

        findViewById<Button>(R.id.change_calendar_btn).setOnClickListener {
            testCalendar.add(Calendar.MONTH, 1)

            calendar.setCalendarDate(testCalendar)
        }
    }
}