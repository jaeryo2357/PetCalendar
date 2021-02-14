package com.minuk.calendar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager

import com.minuk.calendar.databinding.ViewCalendarBinding
import com.minuk.calendar.adapter.MonthConfig
import com.minuk.calendar.adapter.PetCalendarAdapter
import com.minuk.calendar.data.Event

import java.util.*

class PetCalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var calendarBinding: ViewCalendarBinding

    //calendar data property
    private val calendar = Calendar.getInstance()
    private var today = 0
    private var currentMonth = 0
    private var startDayOfWeek = 0
    private var endDayOfCurrentMonth = 31
    private var endDayOfLastMonth = 31
    private var daysSize = DEFAULT_DAYS_SIZE

    //custom view property
    private var viewWidth = 0
    private var viewHeight = 0
    private var childViewWidth = 0
    private var childViewHeight = 0
    private var headerViewHeight = 0f
    private var maxEventCount = 0

    //Ui data property
    private var calendarAccentColor: Int = 0
    private var calendarNormalColor: Int = 0

    private var eventHandler: PetCalendarEventHandler? = null

    private var calendarAdapter: PetCalendarAdapter? = null

    private var monthEventList: List<Event>? = null

    init {
        initLayout(context)
        initCalendarColor(attrs)
        initHeaderSize()
        initEventCount()

        initCalendarData()
        initDayRecyclerView()
    }

    private fun initLayout(context: Context) {
        calendarBinding =
            ViewCalendarBinding.inflate(LayoutInflater.from(context), this)
    }

    private fun initCalendarColor(attrs: AttributeSet?) {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PetCalendarView)

        calendarAccentColor = typedArray.getColor(
            R.styleable.PetCalendarView_calendarColorAccount,
            DEFAULT_ACCENT_COLOR
        )

        calendarNormalColor = typedArray.getColor(
            R.styleable.PetCalendarView_calendarColorNormal,
            DEFAULT_NORMAL_COLOR
        )

        typedArray.recycle()

        calendarBinding.sundayTextView.setTextColor(calendarAccentColor)
        calendarBinding.saturdayTextView.setTextColor(calendarAccentColor)
    }

    private fun initHeaderSize() {
        headerViewHeight = resources.getDimension(R.dimen.height_header)
    }

    private fun initEventCount() {
        val eventRecyclerviewHeight = childViewHeight -
                resources.getDimension(R.dimen.height_dey_tv)

        maxEventCount = (eventRecyclerviewHeight /
                resources.getDimension(R.dimen.height_calendar_event)).toInt() + 1
    }

    private fun initCalendarData() {
        //Log.d(TAG, "year: ${calendar[Calendar.YEAR]} month: ${calendar[Calendar.MONTH]}")
        val tempCalendar = calendar.clone() as Calendar

        today = tempCalendar[Calendar.DAY_OF_MONTH]
        currentMonth = tempCalendar[Calendar.MONTH]
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1)

        startDayOfWeek = tempCalendar[Calendar.DAY_OF_WEEK] - 1
        endDayOfCurrentMonth = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        tempCalendar.add(Calendar.DAY_OF_MONTH, -1)
        endDayOfLastMonth = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        daysSize = startDayOfWeek + endDayOfCurrentMonth
        if (daysSize % DEFAULT_SPAN_SIZE != 0) {
            daysSize += DEFAULT_SPAN_SIZE - daysSize % DEFAULT_SPAN_SIZE
        }
    }

    private fun initDayRecyclerView() {

        val monthConfig = createMonthConfig()

        calendarAdapter = PetCalendarAdapter(
            monthConfig,
            eventHandler
        )

        calendarBinding.monthRecyclerView.apply {
            setItemViewCacheSize(10)
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, DEFAULT_SPAN_SIZE)
            adapter = calendarAdapter
        }
    }

    private fun createMonthConfig() = MonthConfig(
        childViewWidth,
        childViewHeight,
        daysSize,
        calendar[Calendar.YEAR],
        today,
        currentMonth,
        startDayOfWeek,
        endDayOfLastMonth,
        endDayOfCurrentMonth,
        calendarAccentColor,
        calendarNormalColor,
        monthEventList,
        maxEventCount
    )

    /**
     * 측정된 가로와 세로 길이로 ratio를 계산하여
     * 이전 ratio와 다르면 item view 재생성
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> viewWidth = widthMeasureSpec
            MeasureSpec.EXACTLY -> viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        }

        when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> viewHeight = heightMeasureSpec
            MeasureSpec.EXACTLY -> viewHeight = MeasureSpec.getSize(heightMeasureSpec)
        }

        childViewWidth = viewWidth / 7

        val verticalAxisSize = daysSize / 7

        childViewHeight = ((viewHeight - headerViewHeight) / verticalAxisSize).toInt()

        initEventCount()
        initDayRecyclerView()
    }

    fun setCalendarDate(
        calendar: Calendar
    ) {
        setCalendarDate(
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
    }

    fun setCalendarDate(
        year: Int = calendar[Calendar.YEAR],
        month: Int = calendar[Calendar.MONTH],
        day: Int = calendar[Calendar.DAY_OF_MONTH]
    ) {
        calendar.set(year, month, day)

        initCalendarData()
    }

    fun setEventHandler(eventHandler: PetCalendarEventHandler) {
        this.eventHandler = eventHandler

        initDayRecyclerView()
    }

    fun emitEventList(monthEventList: List<Event>) {
        this.monthEventList = monthEventList

        initDayRecyclerView()
    }

    interface PetCalendarEventHandler {
        fun onDayClick(year: Int, month: Int, day: Int)
    }

    companion object {

        private const val TAG = "Pet Calender"

        //달력에서 총 보여질 날짜의 수
        private const val DEFAULT_DAYS_SIZE = 42
        private const val DEFAULT_SPAN_SIZE = 7

        private const val DEFAULT_ACCENT_COLOR = Color.BLACK
        private const val DEFAULT_NORMAL_COLOR = Color.BLACK
    }
}
