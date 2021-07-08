package com.example.today_seyebrowktver.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.today_seyebrowktver.*
import com.example.today_seyebrowktver.R
import com.example.today_seyebrowktver.data.EventData
import com.example.today_seyebrowktver.databinding.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap


data class Flight(
    val time: LocalDateTime,
    val departure: Airport,
    val destination: Airport,
    @ColorRes val color: Int,
) {
    data class Airport(val city: String, val code: String)
}


class Example5FlightsAdapter :
    RecyclerView.Adapter<Example5FlightsAdapter.Example5FlightsViewHolder>() {

    val flights = mutableListOf<Flight>()

    private val formatter = DateTimeFormatter.ofPattern("EEE'\n'dd MMM'\n'HH:mm")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Example5FlightsViewHolder {
        return Example5FlightsViewHolder(
            Example5EventItemViewBinding.inflate(parent.context.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: Example5FlightsViewHolder, position: Int) {
        viewHolder.bind(flights[position])
    }

    override fun getItemCount(): Int = flights.size

    inner class Example5FlightsViewHolder(val binding: Example5EventItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(flight: Flight) {
            binding.itemFlightDateText.apply {
                text = formatter.format(flight.time)
                setBackgroundColor(itemView.context.getColorCompat(flight.color))
            }

            binding.itemDepartureAirportCodeText.text = flight.departure.code
            binding.itemDepartureAirportCityText.text = flight.departure.city

            binding.itemDestinationAirportCodeText.text = flight.destination.code
            binding.itemDestinationAirportCityText.text = flight.destination.city
        }
    }
}

//Class
class Example5Fragment : Fragment() {

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    val mAuth = FirebaseAuth.getInstance()
    private lateinit var uid: String

    private var selectedDate: LocalDate? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

    private val flightsAdapter = Example5FlightsAdapter()
    private val flights = generateFlights().groupBy { it.time.toLocalDate() }

    //    private val eventsAdapter = EventsAdapter()
//    private val events = getEvents()
    var allEventsData: ArrayList<EventData> = ArrayList()
    private var data: ArrayList<EventData> = ArrayList()
    var data2 = arrayListOf<EventData>()
    var mapByDate: LinkedHashMap<String, MutableList<EventData>> = data.groupByTo(LinkedHashMap(),
        { it.date })
    var eventAdapter: RvEventAdapter? = null

    private lateinit var binding: Example5FragmentBinding


    //viewModel
    private lateinit var mainViewModel: ViewModelMain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            mainViewModel = ViewModelProvider(requireActivity()).get(ViewModelMain::class.java)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = Example5FragmentBinding.inflate(inflater, container, false)

//        getEvents()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.exFiveRv.apply {
//            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
////            adapter = flightsAdapter
////            adapter = eventsAdapter
//            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
//        }



        binding.fab3.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO){
                    mainViewModel.insert(EventData("20210708", "1540", false,"김순자",
                    "01030773637", "default",false, "기본", 50000, "현금"
                    , "없음","20200501", "keyValue123"))
                }
//            (activity as ActivityMain).mSelectTypeOfCustomer()

            mainViewModel.getAllEvents().observe(requireActivity(), { events ->
                var tempEventDataList = ArrayList<EventData>()
                for(i in 0 until events.size){
                    tempEventDataList.add(
                        EventData(
                            events[i].date,
                            events[i].time,
                            events[i].complete,
                            events[i].customerName,
                            events[i].customerNumber,
                            events[i].customerGrade,
                            events[i].isRetouch,
                            events[i].menu,
                            events[i].price,
                            events[i].payment,
                            events[i].reservMemo,
                            events[i].savedate,
                            events[i].keyValue
                        )
                    )
                }

                binding.testTv.text = tempEventDataList.size.toString()
            })

        }

        binding.fab2.setOnClickListener {
            val intent = Intent(context, ActivityEachEvent::class.java)
            startActivity(intent)

//            val dlg = DialogEachEvent(requireContext())
//            dlg.start(requireContext())

        }

//        eventsAdapter.notifyDataSetChanged()
//        flightsAdapter.notifyDataSetChanged()

        val daysOfWeek = daysOfWeekFromLocale()
        Log.d("dateCheck", daysOfWeek.toString())

        //현재 달 구하기
        val currentMonth = YearMonth.now()

        // 현재시간을 msec 으로 구한다.
        val now = System.currentTimeMillis()
        // 현재시간을 date 변수에 저장한다.
        val date = Date(now)
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        val sdfNow = SimpleDateFormat("yyyy-MM-dd")
        // nowDate 변수에 값을 저장한다.
        val nowDate: String = sdfNow.format(date)

        Log.d("dateCheck", currentMonth.toString())
        binding.exFiveCalendar.setup(
            currentMonth.minusMonths(100),
            currentMonth.plusMonths(100),
            daysOfWeek.first()
        )
        binding.exFiveCalendar.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = Example5CalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate != day.date) {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            val binding = this@Example5Fragment.binding
                            binding.exFiveCalendar.notifyDateChanged(day.date)
                            oldDate?.let { binding.exFiveCalendar.notifyDateChanged(it) }
//                            updateAdapterForDate(day.date)

                            data.clear()
                            if (mapByDate!![day.date.toString()]?.size ?: 0 != 0) {
                                for (i in 0 until mapByDate!![day.date.toString()]!!.size) {
                                    data.add(mapByDate!![day.date.toString()]!!.get(i))
                                }

                                eventAdapter = RvEventAdapter(data)
                                binding.exFiveRv.layoutManager = LinearLayoutManager(context)
                                binding.exFiveRv.adapter = eventAdapter
                            } else {
                                eventAdapter = RvEventAdapter(data)
                                binding.exFiveRv.layoutManager = LinearLayoutManager(context)
                                binding.exFiveRv.adapter = eventAdapter
                            }

                            Log.d("dateMap", mapByDate!![day.date.toString()].toString() + "?")


                        } else {
                            (activity as ActivityMain).mSelectTypeOfCustomer()
                        }
                    }

                }


            }

        }
        binding.exFiveCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.exFiveDayText
                val layout = container.binding.exFiveDayLayout
                textView.text = day.date.dayOfMonth.toString()

                val newContainer = container.binding.newDot
                val oldContainer = container.binding.oldDot
                val retouchContainer = container.binding.retouchDot
                val skipContainer = container.binding.skipDot

                val eventCountTextView = container.binding.countTv
                newContainer.background = null
                oldContainer.background = null
                retouchContainer.background = null
                skipContainer.background = null


                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.setTextColorRes(R.color.black)
                    layout.setBackgroundResource(if (selectedDate == day.date) R.drawable.example_5_selected_bg else 0)

//                    val events = events[day.date]
                    val flights = flights[day.date]
                    val events = mapByDate!![day.date.toString()]
                    if (events != null) {
                        eventCountTextView.text = events.size.toString() + "개 예약"

                        newContainer.setBackgroundColor(view.context.getColorCompat(R.color.mainYellow))
                        oldContainer.setBackgroundColor(view.context.getColorCompat(R.color.mainGreen))

                    }

//                    if (flights != null) {
//                        if (flights.count() == 1) {
//                            flightBottomView.setBackgroundColor(view.context.getColorCompat(flights[0].color))
//                        } else {
//                            flightTopView.setBackgroundColor(view.context.getColorCompat(flights[0].color))
//                            flightBottomView.setBackgroundColor(view.context.getColorCompat(flights[1].color))
//                        }
//                    }
                } else {
                    textView.setTextColorRes(R.color.mainGreyFor30)
                    layout.background = null
                }

                //오늘 강조하기
                if (day.date.toString() == nowDate) {
                    textView.setBackgroundResource(R.drawable.example_5_today_bg)
//                    textView.setBackgroundColor(view.context.getColorCompat(R.color.mainGreen))
                    textView.setTextColorRes(R.color.white)
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = Example5CalendarHeaderBinding.bind(view).legendLayout.root
        }
        binding.exFiveCalendar.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                    // Setup each header day text if we have not done that already.
                    if (container.legendLayout.tag == null) {
                        container.legendLayout.tag = month.yearMonth
                        container.legendLayout.children.map { it as TextView }
                            .forEachIndexed { index, tv ->
                                tv.text = daysOfWeek[index].getDisplayName(
                                    TextStyle.SHORT,
                                    Locale.ENGLISH
                                )
                                    .toUpperCase(Locale.ENGLISH)
//                        tv.setTextColorRes(R.color.example_5_text_grey)
                                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                            }
                        month.yearMonth
                    }
                }
            }

        binding.exFiveCalendar.monthScrollListener = { month ->
            val title = "${monthTitleFormatter.format(month.yearMonth)} ${month.yearMonth.year}"
            binding.exFiveMonthYearText.text = title

            selectedDate?.let {
                // Clear selection if we scroll to a new month.
                selectedDate = null
                binding.exFiveCalendar.notifyDateChanged(it)
                updateAdapterForDate(null)
            }
        }

        binding.exFiveNextMonthImage.setOnClickListener {
            binding.exFiveCalendar.findFirstVisibleMonth()?.let {
                binding.exFiveCalendar.smoothScrollToMonth(it.yearMonth.next)
            }
        }

        binding.exFivePreviousMonthImage.setOnClickListener {
            binding.exFiveCalendar.findFirstVisibleMonth()?.let {
                binding.exFiveCalendar.smoothScrollToMonth(it.yearMonth.previous)
            }
        }

        binding.menuIcon.setOnClickListener {
            val intent = Intent(context, ActivityHomeMenu::class.java)
            startActivity(intent)
        }

        binding.exFiveMonthYearText.setOnClickListener {
            val dialog = AlertDialog.Builder(context).create()
            val edialog: LayoutInflater = LayoutInflater.from(context)
            val mView: View = edialog.inflate(R.layout.dialog_custom_datepicker, null)

            val year: NumberPicker = mView.findViewById(R.id.year_picker)
            val month: NumberPicker = mView.findViewById(R.id.month_picker)
            val cancel: TextView = mView.findViewById(R.id.cancel_btn)
            val ok: TextView = mView.findViewById(R.id.ok_btn)

            //  순환 안되게 막기
            year.wrapSelectorWheel = false
            month.wrapSelectorWheel = false

            //  editText 설정 해제
            year.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            month.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            //  최소값 설정
            year.minValue = 2015
            month.minValue = 1


//             현재시간을 msec 으로 구한다.
            val now = System.currentTimeMillis()
            // 현재시간을 date 변수에 저장한다.
            val date = Date(now)
            // 시간을 나타냇 포맷을 정한다 ( yyyy
            val formatYear = SimpleDateFormat("yyyy")
            val formatMonth = SimpleDateFormat("MM")
            // nowDate 변수에 값을 저장한다.
            val formatDateYear = formatYear.format(date)
            val formatDateMonth = formatMonth.format(date)
            val maxYearInt = Integer.parseInt(formatDateYear)+5

            //  최대값 설정
            year.maxValue = maxYearInt //올해 + 5
            month.maxValue = 12


            //보여지는 값 세팅 ; output은 다름
//            hour.displayedValues = arrayOf("06시")

            month.displayedValues = arrayOf("01", "02", "03", "04", "05", "06", "07" , "08", "09", "10", "11", "12")

            month.setOnValueChangedListener { picker, oldVal, newVal ->
            }

            //초기값 세팅
            year.value = Integer.parseInt(formatDateYear) //현재 년도
            month.value = Integer.parseInt(formatDateMonth)

            //  취소 버튼 클릭 시
            cancel.setOnClickListener {
                dialog.dismiss()
                dialog.cancel()
            }

            //  완료 버튼 클릭 시
            ok.setOnClickListener {

                Toast.makeText(context, year.value.toString() +"년" + month.value +"월", Toast.LENGTH_SHORT).show()

//                var dspHour: String = ""
//                when (hour.value) {
//                    6 -> dspHour = "06"
//                    7 -> dspHour = "07"
//                    8 -> dspHour = "08"
//                    9 -> dspHour = "09"
//                }
//
//                var dspMin: String = ""
//                when (minute.value) {
//                    0 -> dspMin = "00분"
//                    1 -> dspMin = "10분"
//                    2 -> dspMin = "20분"
//                    3 -> dspMin = "30분"
//                    4 -> dspMin = "40분"
//                    5 -> dspMin = "50분"
//
//                }
//
//                if (hour.value < 10) {
//                    binding.timeContentTv.text = dspHour + "시 " + dspMin
//                } else {
//                    binding.timeContentTv.text =
//                        (hour.value).toString() + "시 " + dspMin
//                }


                dialog.dismiss()
                dialog.cancel()
            }

            dialog.setView(mView)
            dialog.create()
            dialog.show()

            //크기조정
            val dm = resources.displayMetrics
            val width = (dm.widthPixels * 0.7).toInt() // Display 사이즈의 90%
            val params: ViewGroup.LayoutParams = dialog.getWindow()!!.getAttributes()
            params.width = width
            dialog.getWindow()!!.setAttributes(params as WindowManager.LayoutParams)
        }


    }

    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor = requireContext().getColorCompat(R.color.white)
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.statusBarColor = requireContext().getColorCompat(R.color.white)
    }

    private fun updateAdapterForDate(date: LocalDate?) {
//        eventsAdapter.events.clear()
//        eventsAdapter.events.addAll(events[date])

//        flightsAdapter.flights.clear()
//        flightsAdapter.flights.addAll(flights[date].orEmpty())
//        flightsAdapter.notifyDataSetChanged()
    }

//    fun getEvents() {
//        val user = mAuth.currentUser
//        uid = user!!.uid
//
//        database.child("users").child(uid).child("eventsByDate").child("2021-04")
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    val newEvent2: ArrayList<EventData> = ArrayList()
//                    for (ds in dataSnapshot.children) {
//                        val eventData: EventData? = ds.getValue(EventData::class.java)
//                        if (eventData != null) {
//                            newEvent2.add(eventData)
//                        }
//                    }
//                    data.clear()
//                    data.addAll(newEvent2)
////                    Log.d("dataList", data[0].date)
////                    Log.d("dataList", data[1].date)
////                    Log.d("dataList", data[2].date)
//
//
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//            })
//    }


//        val events = ArrayList<EventData>()
//        val currentMonth = YearMonth.now()
//
//        val currentMonth17 = currentMonth.atDay(17)
//        events.add(
//            EventData(
//                "2021-04-17", "1430", true, "김순자", "01030773637", "new", false,
//                "eye", "20000", "cash", "없음", "20210420"
//            ,"asdasd")
//        )
//
//        events.add(
//            EventData(
//                "2021-04-21", "1800", true, "나계환", "01030773637", "new", false,
//                "eye", "20000", "cash", "없음", "20210420"
//                ,"asdasd" )
//        )
//
//        events.add(
//            EventData(
//                "2021-05-12", "1800", true, "나계환", "01030773637", "old", false,
//                "eye", "20000", "cash", "없음", "20210420"
//                ,"asdasd")
//        )
//
//        events.add(
//            EventData(
//                "2021-04-30", "1800", true, "나계환", "01030773637", "old", true,
//                "eye", "20000", "cash", "없음", "20210420"
//                ,"asdasd" )
//        )
//
//        return events

}
