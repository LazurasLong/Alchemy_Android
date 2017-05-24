package com.etbrady.alchemy.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.etbrady.alchemy.R
import com.etbrady.alchemy.adapters.ClassAdapter
import com.etbrady.alchemy.apis.AlchemyAPI
import com.etbrady.alchemy.models.Class
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import com.github.salomonbrys.kotson.*
import kotlinx.android.synthetic.main.fragment_schedule.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleFragment : Fragment() {

    private var date: Date? = null
    private val alchemyAPI: AlchemyAPI
    private val classAdapter = ClassAdapter()

    init {
        alchemyAPI = createAlchemyAPI()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            date = Date(arguments.getLong(DATE_KEY))
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_schedule, container, false)

        val classesRecyclerView = view.classes_recycler_view
        classesRecyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(context)
        classesRecyclerView.layoutManager = layoutManager

        classesRecyclerView.adapter = classAdapter


        val currentDateStringPair = getCurrentDateStringPair()
        val call = alchemyAPI.getClasses(currentDateStringPair.first, currentDateStringPair.second)
        call.enqueue(object: Callback<List<Class>> {
            override fun onResponse(call: Call<List<Class>>?, response: Response<List<Class>>?) {
                classAdapter.setClasses(response!!.body())
            }

            override fun onFailure(call: Call<List<Class>>?, t: Throwable?) {
                print("On Failure!")
            }
        })
        return view
    }

    private fun getCurrentDateStringPair(): Pair<String,String> {
        val calendar = Calendar.getInstance()

        val startDateString = getDateString(calendar, 0, 0, 0)
        val endDateString = getDateString(calendar, 23, 59, 59)

        return Pair(startDateString, endDateString)
    }

    private fun getDateString(calendar: Calendar, hour: Int, minute: Int, second: Int): String {
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, second)

        val date = calendar.time
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'", Locale.US)

        return dateFormatter.format(date)
    }

    private fun createAlchemyAPI(): AlchemyAPI {

        val gson = GsonBuilder()
                .registerTypeAdapter<List<Class>> {
                    deserialize({
                        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'", Locale.US)
                        it.json["event_occurrences"].asJsonArray.map {
                            val name = it["name"].asString
                            val startDate = dateFormatter.parse(it["start_at"].asString)
                            val endDate = dateFormatter.parse(it["end_at"].asString)
                            val locationId = it["location_id"].asInt
                            val instructors = it["staff_members"].asJsonArray
                            val instructorName = if (instructors.size() > 0) {
                                instructors[0]["name"].asString
                            } else {
                                ""
                            }
                            Class(name, startDate, endDate, locationId, instructorName)
                        }
                    })

                }
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://alchemy365.frontdeskhq.com/api/v2/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        return retrofit.create(AlchemyAPI::class.java)
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
