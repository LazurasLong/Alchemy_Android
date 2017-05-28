package com.etbrady.alchemy.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.etbrady.alchemy.R
import com.etbrady.alchemy.adapters.EventAdapter
import com.etbrady.alchemy.apis.AlchemyFrontDeskAPIFactory
import com.etbrady.alchemy.models.Event
import kotlinx.android.synthetic.main.fragment_schedule.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ScheduleFragment : Fragment(), DateListener {

    private var date: Date? = null
    private val eventAdapter = EventAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            date = Date(arguments.getLong(DATE_KEY))
        }
    }

    override fun onResume() {
        super.onResume()
        loadEvents()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_schedule, container, false)

        val eventsRecyclerView = view.events_recycler_view
        eventsRecyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(context)
        eventsRecyclerView.layoutManager = layoutManager

        eventsRecyclerView.adapter = eventAdapter

        return view
    }

    override fun setDate(date: Date) {
        this.date = date
        loadEvents()
    }

    private fun loadEvents() {
        val currentDateStringPair = getSetDateStringPair()
        val alchemyAPI = AlchemyFrontDeskAPIFactory.createAlchemyFrontDeskAPIInstance(context)
        val call = alchemyAPI.getEvents(currentDateStringPair.first, currentDateStringPair.second)
        call.enqueue(object: Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>?, response: Response<List<Event>>?) {
                if (response != null) {
                    val events = response.body()
                    val sortedEvents = events.sortedBy { it.startDate }
                    eventAdapter.setEvents(sortedEvents)
                }
            }

            override fun onFailure(call: Call<List<Event>>?, t: Throwable?) {
                print("On Failure!")
            }
        })
    }

    private fun getSetDateStringPair(): Pair<String,String> {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date?.time ?: calendar.timeInMillis
        val startDateString = getDateString(calendar, 0, 0, 0)
        val endDateString = getDateString(calendar, 23, 59, 59)

        return Pair(startDateString, endDateString)
    }

    private fun getDateString(calendar: Calendar, hour: Int, minute: Int, second: Int): String {
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, second)

        val date = calendar.time
        val dateFormatter = SimpleDateFormat(getString(R.string.alchemy_date_format), Locale.US)

        return dateFormatter.format(date)
    }

    companion object {

        private val DATE_KEY = "date_key"

        fun newInstance(date: Date): ScheduleFragment {
            val fragment = ScheduleFragment()
            val args = Bundle()
            args.putLong(DATE_KEY, date.time)
            fragment.arguments = args
            return fragment
        }
    }
}
