package com.poochaser.poochaser.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.poochaser.poochaser.R
import com.poochaser.poochaser.ui.clock.CalendarAdapter
import com.poochaser.poochaser.ui.clock.Data
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_clock.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel
    lateinit var calendarAdapter: CalendarAdapter
    lateinit var five : ArrayList<CalendarDay>
    lateinit var three : ArrayList<CalendarDay>
    lateinit var one : ArrayList<CalendarDay>
    val user = Firebase.auth.currentUser
    val db = FirebaseFirestore.getInstance()
    var year = ""
    var month = ""
    var day = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendarViewModel =
            ViewModelProviders.of(this).get(CalendarViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_calendar, container, false)
        five = ArrayList<CalendarDay>()
        three = ArrayList<CalendarDay>()
        one = ArrayList<CalendarDay>()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendarView.setWeekDayLabels(arrayOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"))
        calendarView.setOnDateChangedListener { widget, date, selected ->
            val timeFormat = SimpleDateFormat("yyyy")
            val selecedDate = calendarView.selectedDate!!
            val localDate = LocalDate.of(selecedDate.year, selecedDate.month, selecedDate.day)
            val defaultZoneId = ZoneId.systemDefault()
            val date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant())
            year = timeFormat.format(date)
            timeFormat.applyPattern("MM")
            month = timeFormat.format(date)
            timeFormat.applyPattern("dd")
            day = timeFormat.format(date)
            dataTransfer()
        }
        calendarView.setOnMonthChangedListener { widget, date ->
            val timeFormat = SimpleDateFormat("yyyy")
            val localDate = LocalDate.of(date.year, date.month, date.day)
            val defaultZoneId = ZoneId.systemDefault()
            val date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant())
            year = timeFormat.format(date)
            timeFormat.applyPattern("MM")
            month = timeFormat.format(date)
            timeFormat.applyPattern("dd")
            day = timeFormat.format(date)
            calendarAdapter.datas.clear()
            calendarAdapter.notifyDataSetChanged()
            dataCheck()
        }
        calendarView.addDecorator(DateSelectorDecorator(requireActivity()))
        calendarAdapter = CalendarAdapter(requireContext())
        calendarRecyclerView.adapter = calendarAdapter
        val timeFormat = SimpleDateFormat("yyyy")
        year = timeFormat.format(System.currentTimeMillis())
        timeFormat.applyPattern("MM")
        month = timeFormat.format(System.currentTimeMillis())
        timeFormat.applyPattern("dd")
        day = timeFormat.format(System.currentTimeMillis())
        dataTransfer()
        dataCheck()
        calendarView.setDateSelected(CalendarDay.today(),true)
    }

    fun time24to12(time: String): String{
        if(time.substring(0,2).toInt() > 12) {
            if((time.substring(0, 2).toInt() - 12) < 10)
                return "0" + (time.substring(0, 2).toInt() - 12) + time.substring(2, 5) + " PM"
            else
                return "" + (time.substring(0, 2).toInt() - 12) + time.substring(2, 5) + " PM"
        }
        else
            return time + " AM"
    }

    fun dataTransfer(){
        calendarAdapter.datas.clear()
        db.collection(user!!.uid).document(year).collection(month).document(day)
            .collection("data").get().addOnSuccessListener { documents ->
                for(document in documents){
                    calendarAdapter.datas.add(
                        Data(time24to12(document.id),
                        document.get("type").toString().toInt(), document.getString("color")!!)
                    )
                }
                calendarAdapter.notifyDataSetChanged()
            }
    }

    fun dataCheck(){
        var startDay = 1
        var endDay = 30
        if (month.toInt() == 2){
            if (year.toInt() % 4 == 0 &&
                    year.toInt() % 100 != 0 &&
                    year.toInt() % 400 == 0)
                        endDay = 29
            else
                endDay = 28
        }
        else if (month.toInt() == 1 ||
            month.toInt() == 3 ||
            month.toInt() == 5 ||
            month.toInt() == 7 ||
            month.toInt() == 8 ||
            month.toInt() == 10 ||
            month.toInt() == 12)
                endDay = 31
        for (i in startDay..endDay){
            val timeFormat = SimpleDateFormat("dd")
            val localDate = LocalDate.of(year.toInt(), month.toInt(), i)
            val defaultZoneId = ZoneId.systemDefault()
            val date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant())
            val currentDay = timeFormat.format(date)
            db.collection(user!!.uid).document(year).collection(month).document(currentDay)
                .collection("data").get().addOnSuccessListener { documents ->
                    if(documents.size()>4)
                        five.add(CalendarDay.from(year.toInt(), month.toInt(), currentDay.toInt()))
                    else if(documents.size()>2)
                        three.add(CalendarDay.from(year.toInt(), month.toInt(), currentDay.toInt()))
                    else if(documents.size()!=0)
                        one.add(CalendarDay.from(year.toInt(), month.toInt(), currentDay.toInt()))
                    calendarView.addDecorator(OneSelectorDecorator(one,requireActivity()))
                    calendarView.addDecorator(ThreeSelectorDecorator(three,requireActivity()))
                    calendarView.addDecorator(FiveSelectorDecorator(five,requireActivity()))
                    calendarView.selectedDate = calendarView.selectedDate
                }
        }
    }

    override fun onResume() {
        super.onResume()
        calendarView.selectedDate = CalendarDay.today()
    }
}