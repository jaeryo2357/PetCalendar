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
import com.minuk.calendar.ui.MonthConfig
import com.minuk.calendar.ui.PetCalendarAdapter
import java.time.Month

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

    //Ui data property
    private var calendarAccentColor: Int = 0
    private var calendarNormalColor: Int = 0

    private var eventHandler: PetCalendarEventHandler? = null

    private var calendarAdapter: PetCalendarAdapter? = null

    init {
        initLayout(context)
        initCalendarColor(attrs)
        initHeaderSize()

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

        calendarBinding.sundayTextview.setTextColor(calendarAccentColor)
        calendarBinding.saturdayTextview.setTextColor(calendarAccentColor)
    }

    private fun initHeaderSize() {
        headerViewHeight = resources.getDimension(R.dimen.height_header)
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

        calendarAdapter?.run {
            monthConfig = createMonthConfig()
            notifyDataSetChanged()
        }
    }

    private fun initDayRecyclerView() {

        val monthConfig = createMonthConfig()

        calendarAdapter = PetCalendarAdapter(
            monthConfig,
            eventHandler
        )

        calendarBinding.daysRecyclerview.apply {
            setItemViewCacheSize(10)
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, DEFAULT_SPAN_SIZE)
            adapter = calendarAdapter

            val dividerItemDecoration = DividerItemDecoration(context, VERTICAL)

            ResourcesCompat.getDrawable(resources, R.drawable.bg_divider, null)?.let {
                dividerItemDecoration.setDrawable(it)
            }

            addItemDecoration(dividerItemDecoration)
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
            calendarNormalColor)

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

        calendarAdapter?.run {
            monthConfig = createMonthConfig()
            notifyDataSetChanged()
        }
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
