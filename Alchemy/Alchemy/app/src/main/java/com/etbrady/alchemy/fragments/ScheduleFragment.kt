package com.etbrady.alchemy.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.etbrady.alchemy.R
import kotlinx.android.synthetic.main.fragment_schedule.*
import java.util.*


class ScheduleFragment : Fragment() {

    private var date: Date? = null

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
        //date_textview.text = date.toString()
        return view
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
