package com.etbrady.alchemy.models

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
}
