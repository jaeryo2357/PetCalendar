package com.minuk.calendar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat

import androidx.recyclerview.widget.RecyclerView
import com.minuk.calendar.databinding.ItemCalendarBinding

import com.minuk.calendar.databinding.ViewCalendarBinding

import java.util.*
import kotlin.math.max
import kotlin.math.min

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
    private var headerViewHeight = 0f
    private var itemViewRatio = DEFAULT_ITEM_DIMENS_RATIO

    //Ui data property
    private var weekendColor = DEFAULT_WEEKEND_COLOR
    private var weekdaysColor = DEFAULT_WEEKDAYS_COLOR
    private var todayColor = DEFAULT_TODAY_COLOR


    private var calendarAdapter: PetCalendarAdapter? = null

    init {
        initLayout(context)
        initHeaderSize()

        initCalendarData()
        initDayRecyclerView()
    }

    private fun initLayout(context: Context) {
        val inflater = LayoutInflater.from(context)

        calendarBinding = ViewCalendarBinding.inflate(inflater)

        this.addView(calendarBinding.root)
    }

    private fun initHeaderSize() {
        headerViewHeight = resources.getDimension(R.dimen.height_header)
    }

    private fun initCalendarData() {
        Log.d(TAG, "year: ${calendar[Calendar.YEAR]} month: ${calendar[Calendar.MONTH]}")

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

        if (calendarAdapter == null) {
            calendarAdapter = PetCalendarAdapter()
        } else {
            calendarAdapter?.notifyDataSetChanged()
        }
    }

    private fun initDayRecyclerView() {
        calendarBinding.daysRecyclerview.apply {
            setItemViewCacheSize(10)
            adapter = calendarAdapter
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        when(MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> viewWidth = widthMeasureSpec
            MeasureSpec.EXACTLY -> viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        }

        when(MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> viewHeight = heightMeasureSpec
            MeasureSpec.EXACTLY -> viewHeight = MeasureSpec.getSize(heightMeasureSpec)
        }

        val ratio = getItemViewDimensRatio()

        if (itemViewRatio != ratio) {
            itemViewRatio = ratio

            //저장된 ViewHolder 캐시를 지워서 새로운 크기의 ViewHolder 생성
            calendarBinding.daysRecyclerview.recycledViewPool.clear()
            calendarAdapter?.notifyDataSetChanged()
        }
    }

    public fun setCalendar(
        calendar: Calendar
    ) {
        setCalendar(
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
    }

    public fun setCalendar(
        year: Int = calendar[Calendar.YEAR],
        month: Int = calendar[Calendar.MONTH],
        day: Int = calendar[Calendar.DAY_OF_MONTH]
    ) {
        calendar.set(year, month, day)

        initCalendarData()
    }

    /**
     * ConstraintLayout DimensRatio 구하는 함수
     *
     * @return width:height  ex) 2:3
     */
    private fun getItemViewDimensRatio(): String {
        var verticalAxisSize = daysCount / 7

        val itemViewWidth = viewWidth / 7
        val itemViewHeight = (viewHeight - headerViewHeight) / verticalAxisSize

        val gcd = getGCD(itemViewWidth, itemViewHeight.toInt())
        //Log.d(TAG, "width: $itemViewWidth, height: $itemViewHeight, gcd: $gcd")

        return "${itemViewWidth / gcd}:${(itemViewHeight / gcd).toInt()}"
    }

    /**
     * 유클리드 호제법으로 구하는 a와 b의 최대공약
     */
    private fun getGCD(a: Int, b: Int): Int {
        var maximum = max(a, b)
        var minimum = min(a, b)

        while (minimum != 0) {
            val r = maximum % minimum

            if (r == 0) return minimum

            maximum = b
            minimum = r
        }

        return minimum
    }

    private inner class PetCalendarAdapter :
        RecyclerView.Adapter<PetCalendarAdapter.PetCalenderViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetCalenderViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return PetCalenderViewHolder(ItemCalendarBinding.inflate(inflater))
        }

        override fun onBindViewHolder(holder: PetCalenderViewHolder, position: Int) {
            val day: Int = when {
                position < startDayOfWeek -> {
                    endDayOfLastMonth - (startDayOfWeek - position - 1)
                }
                position > (endDayOfCurrentMonth + startDayOfWeek - 1) -> {
                    position - (endDayOfCurrentMonth + startDayOfWeek) + 1
                }
                else -> {
                    position - startDayOfWeek + 1
                }
            }

            holder.binding.dayCalendarTv.text = "$day"

            //현재 요일의 경우 동그라미 이미지 표시
            if (position == toDay + startDayOfWeek - 1) {
                holder.binding.dayCalendarTv.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.bg_date_selected, null)
            } else {
                holder.binding.dayCalendarTv.background = null
            }

            //주말과 평일, 오늘 각각의 text color 설정
            if (position % 7 == 0 || position % 7 == 6) {
                holder.binding.dayCalendarTv.setTextColor(weekendColor)
            } else if (position == toDay + startDayOfWeek - 1){
                holder.binding.dayCalendarTv.setTextColor(todayColor)
            } else {
                holder.binding.dayCalendarTv.setTextColor(weekdaysColor)
            }
        }

        override fun getItemCount(): Int {
            return daysCount
        }

        private inner class PetCalenderViewHolder(
            val binding: ItemCalendarBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            init {
                val layoutParams =
                    binding.dayCalendarCl.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.dimensionRatio = itemViewRatio
               // Log.d(TAG, itemViewRatio)

                binding.dayCalendarCl.layoutParams = layoutParams

            }
        }
    }

    companion object {

        const val TAG = "Pet Calender"
        //달력에서 총 보여질 날짜의 수
        const val DEFAULT_DAYS_COUNT = 42

        const val DEFAULT_ITEM_DIMENS_RATIO = "2:3"

        val DEFAULT_WEEKEND_COLOR = Color.parseColor("#1d6a96")
        val DEFAULT_WEEKDAYS_COLOR = Color.parseColor("#283b42")
        const val DEFAULT_TODAY_COLOR = Color.WHITE
    }
}
