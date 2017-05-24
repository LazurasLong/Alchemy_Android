package com.etbrady.alchemy.models

import java.text.SimpleDateFormat
import java.util.*

class Class (
        val name: String,
        val startDate: Date,
        val endDate: Date,
        val locationId: Int,
        val instructorName: String
)  {

    fun getLocationName(): String {
        return "Alchemy North Loop"
    }

    fun getStartDateString(): String {
        return formatDate(startDate)
    }

    fun getEndDateString(): String {
        return formatDate(endDate)
    }

    private fun formatDate(date: Date): String {
        val dateFormatter = SimpleDateFormat("h:mm a", Locale.US)
        return dateFormatter.format(date)
    }
}
