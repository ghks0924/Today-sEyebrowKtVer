package com.example.today_seyebrowktver

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

class Example5Fragment : Fragment() {

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    val mAuth = FirebaseAuth.getInstance()
    private lateinit var uid : String

    private var selectedDate: LocalDate? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

    private val flightsAdapter = Example5FlightsAdapter()
    private val flights = generateFlights().groupBy { it.time.toLocalDate() }

    //    private val eventsAdapter = EventsAdapter()
//    private val events = getEvents()
    var allEventsData : ArrayList<EventData> = ArrayList()
    private var data: ArrayList<EventData> = ArrayList()
    var data2 = arrayListOf<EventData>()
    var mapByDate: LinkedHashMap<String, MutableList<EventData>> = data.groupByTo(LinkedHashMap(), { it.date })
    var eventAdapter: RvEventAdapter? = null

    private lateinit var binding: Example5FragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = Example5FragmentBinding.inflate(inflater, container, false)



        getEvents()




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

        binding.fab.setOnClickListener(View.OnClickListener {
            (activity as ActivityMain).mSelectTypeOfCustomer()

        })
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


                        } else{
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
                if (day.date.toString() == nowDate){
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

    fun getEvents() {
        val user = mAuth.currentUser
        uid = user.uid

        database.child("users").child(uid).child("events").child("2021-04")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val newEvent2: ArrayList<EventData> = ArrayList()
                    for (ds in dataSnapshot.children) {
                        val eventData: EventData? = ds.getValue(EventData::class.java)
                        if (eventData != null) {
                            newEvent2.add(eventData)
                        }
                    }
                    data.clear()
                    data.addAll(newEvent2)
                    Log.d("dataList", data[0].date)
                    Log.d("dataList", data[1].date)
                    Log.d("dataList", data[2].date)


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }


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
