package com.minuk.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout

class PetCalendarView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        initLayout(context)
    }

    private fun initLayout(context: Context) {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_calendar, this)
    }
}
