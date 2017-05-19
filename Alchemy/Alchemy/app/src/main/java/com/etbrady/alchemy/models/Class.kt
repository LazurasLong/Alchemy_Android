package com.etbrady.alchemy.models

import java.util.*
import com.google.gson.annotations.SerializedName

class Class (
        @SerializedName("name")
        val name: String,
        @SerializedName("start_at")
        val startDate: Date,
        @SerializedName("end_at")
        val endDate: Date,
        @SerializedName("location_id")
        val locationId: Int,
        @SerializedName("staff_members")
        val instructorName: String
) {
    fun getLocationName(): String {
        return "Alchemy North Loop"
    }
}
