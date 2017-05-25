package com.etbrady.alchemy.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.etbrady.alchemy.R
import com.etbrady.alchemy.adapters.WorkoutAdapter
import com.etbrady.alchemy.apis.AlchemyAPIFactory
import com.etbrady.alchemy.models.Workout
import kotlinx.android.synthetic.main.fragment_workouts.view.*
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import org.jsoup.safety.Whitelist



class WorkoutFragment: Fragment(), DateListener {

    private var date: Date? = null
    private val workoutAdapter = WorkoutAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            date = Date(arguments.getLong(WorkoutFragment.DATE_KEY))
        }
    }

    override fun onResume() {
        super.onResume()
        loadWorkouts()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_workouts, container, false)

        val classesRecyclerView = view.workouts_recycler_view
        classesRecyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(context)
        classesRecyclerView.layoutManager = layoutManager

        classesRecyclerView.adapter = workoutAdapter

        return view
    }

    override fun setDate(date: Date) {
        this.date = date
        loadWorkouts()
    }

    private fun loadWorkouts() {
        val alchemyAPI = AlchemyAPIFactory.createAlchemyAPIInstance(context)
        val dateString = getSetDateString()
        val call = alchemyAPI.getWorkouts(dateString)
        call.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                if (response != null) {
                    val workoutNames = arrayOf("A10","A20","AStrong","APulse")
                    val html = response.body().string()

                    val doc = Jsoup.parse(html)
                    val workoutsHtml = doc.select(".single-content")[0]
                    workoutsHtml.select("br").append("\\n")
                    val workoutsString = workoutsHtml.html().replace("\\\\n", "\n")
                    val cleanedWorkoutsString = Jsoup.clean(workoutsString, "", Whitelist.none(), Document.OutputSettings().prettyPrint(false))
                    val workoutStringArray = cleanedWorkoutsString.split("\n").filter {
                        !it.contains("________")
                    }.map {
                        it.trim()
                    }.groupBy {
                        workoutNames.contains(it)
                    }.map {
                        it.value
                    }

                    val workouts = workoutStringArray[0].zip(workoutStringArray[1])
                            .map {
                                val title = it.first
                                val exercises = it.second.split("\\n")
                                Workout(title, exercises)
                            }
                    workoutAdapter.setWorkouts(workouts)
                }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                print("On Failure!")
            }
        })
    }

    private fun getSetDateString(): String {
        val dateFormatter = SimpleDateFormat("yyyy/MM/EEEE-MMddyy", Locale.US)
        return dateFormatter.format(date)
    }

    companion object {

        private val DATE_KEY = "date_key"

        fun newInstance(date: Date): WorkoutFragment {
            val fragment = WorkoutFragment()
            val args = Bundle()
            args.putLong(DATE_KEY, date.time)
            fragment.arguments = args
            return fragment
        }
    }
}