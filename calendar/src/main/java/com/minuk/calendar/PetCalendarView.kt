package com.minuk.calendar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat

import androidx.recyclerview.widget.RecyclerView
import com.minuk.calendar.databinding.ItemCalendarBinding

import com.minuk.calendar.databinding.ViewCalendarBinding

import java.util.*

class PetCalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var calendarBinding: ViewCalendarBinding

    //calendar data property
    private val calendar = Calendar.getInstance()
    private var toDay = 0
    private var startDayOfWeek = 0
    private var endDayOfCurrentMonth = 31
    private var endDayOfLastMonth = 31
    private var daysCount = DEFAULT_DAYS_COUNT

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

        toDay = calendar[Calendar.DAY_OF_MONTH]
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        startDayOfWeek = calendar[Calendar.DAY_OF_WEEK] - 1
        endDayOfCurrentMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        calendar.add(Calendar.DAY_OF_MONTH, -1)
        endDayOfLastMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        daysCount = startDayOfWeek + endDayOfCurrentMonth
        if (daysCount % 7 != 0) {
            daysCount += 7 - daysCount % 7
        }

        calendarAdapter?.notifyDataSetChanged()
    }

    private fun initDayRecyclerView() {
        calendarAdapter = PetCalendarAdapter()

        calendarBinding.daysRecyclerview.apply {
            setItemViewCacheSize(10)
            adapter = calendarAdapter
        }
    }

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

        val verticalAxisSize = daysCount / 7

        childViewHeight = ((viewHeight - headerViewHeight) / verticalAxisSize).toInt()
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

    private inner class PetCalendarAdapter :
        RecyclerView.Adapter<PetCalendarAdapter.PetCalenderViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetCalenderViewHolder {
            return PetCalenderViewHolder(
                ItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

        override fun onBindViewHolder(holder: PetCalenderViewHolder, position: Int) {

            val day: Int = getDayFromPosition(calendar[Calendar.MONTH], position).second

            holder.binding.dayCalendarTv.text = "$day"

            //현재 요일의 경우 동그라미 이미지 표시
            if (position == toDay + startDayOfWeek - 1) {
                val drawable = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.bg_date_selected, null
                )?.apply {
                    DrawableCompat.setTint(this, calendarAccentColor)
                }

                holder.binding.dayCalendarTv.background = drawable
            } else {
                holder.binding.dayCalendarTv.background = null
            }


            if (position == toDay + startDayOfWeek - 1) {
                holder.binding.dayCalendarTv.setTextColor(Color.WHITE)
            } else if (position % 7 == 0 || position % 7 == 6) {
                holder.binding.dayCalendarTv.setTextColor(calendarAccentColor)
            } else {
                holder.binding.dayCalendarTv.setTextColor(calendarNormalColor)
            }
        }

        override fun getItemCount(): Int {
            return daysCount
        }

        /**
         * position 값으로 날짜 측정하는 함수
         * 한 달력 화면에서 최대 3개의 월 달력이 보여 월과 일을 구분
         *
         * @return month 와 day 를 Pair 객체로 반환합니다.
         */
        private fun getDayFromPosition(
            currentMonth: Int = calendar[Calendar.MONTH],
            position: Int
        ): Pair<Int, Int> = when {
            position < startDayOfWeek -> {
                val day = endDayOfLastMonth - (startDayOfWeek - position - 1)
                Pair(currentMonth - 1, day)
            }
            position > (endDayOfCurrentMonth + startDayOfWeek - 1) -> {
                val day = position - (endDayOfCurrentMonth + startDayOfWeek) + 1
                Pair(currentMonth, day)
            }
            else -> {
                val day = position - startDayOfWeek + 1
                Pair(currentMonth + 1, day)
            }
        }

        private inner class PetCalenderViewHolder(
            val binding: ItemCalendarBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            init {
                initLayout()
                initEventHandler()
            }

            private fun initLayout() {
                val layoutParams = binding.root.layoutParams.apply {
                    width = childViewWidth
                    height = childViewHeight
                }
                // Log.d(TAG, itemViewRatio)

                binding.root.layoutParams = layoutParams
            }

            private fun initEventHandler() {
                itemView.setOnClickListener {
                    val year = calendar[Calendar.YEAR]

                    val (month, day) = getDayFromPosition(position = adapterPosition)

                    eventHandler?.onDayClick(year, month, day)
                }
            }
        }
    }

    interface PetCalendarEventHandler {
        fun onDayClick(year: Int, month: Int, day: Int)
    }

    companion object {

        private const val TAG = "Pet Calender"

        //달력에서 총 보여질 날짜의 수
        private const val DEFAULT_DAYS_COUNT = 42

        private const val DEFAULT_ACCENT_COLOR = Color.BLACK
        private const val DEFAULT_NORMAL_COLOR = Color.BLACK
    }
}
