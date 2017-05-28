package com.etbrady.alchemy.apis

import android.content.Context
import com.etbrady.alchemy.R
import com.etbrady.alchemy.models.Event
import com.github.salomonbrys.kotson.DeserializerArg
import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.registerTypeAdapter
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class AlchemyFrontDeskAPIFactory {
    companion object {
        fun createAlchemyFrontDeskAPIInstance(context: Context): AlchemyFrontDeskAPI {
            val gson = GsonBuilder()
                    .registerTypeAdapter<List<Event>> {
                        deserialize({
                            deserializeClassList(it, context)
                        })
                    }
                    .create()

            val retrofit = Retrofit.Builder()
                    .baseUrl(context.getString(R.string.alchemy_front_desk_base_url))
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

            return retrofit.create(AlchemyFrontDeskAPI::class.java)
        }

        private fun deserializeClassList(deserializerArg: DeserializerArg, context: Context): List<Event> {
            val dateFormatter = SimpleDateFormat(context.getString(R.string.alchemy_date_format), Locale.US)
            dateFormatter.timeZone = TimeZone.getTimeZone("UTC")
            return deserializerArg.json["event_occurrences"].asJsonArray.map {
                val name = it["name"].asString
                val startDate = dateFormatter.parse(it["start_at"].asString)
                val endDate = dateFormatter.parse(it["end_at"].asString)
                val locationId = it["location_id"].asInt
                val locationName = when (locationId) {
                    10514 -> "Alchemy North Loop"
                    18997 -> "Alchemy Northeast"
                    27577 -> "Alchemy Edina"
                    else -> ""
                }
                val instructors = it["staff_members"].asJsonArray
                val instructorName = if (instructors.size() > 0) {
                    instructors[0]["name"].asString
                } else {
                    ""
                }
                Event(name, startDate, endDate, locationName, instructorName)
            }
        }
    }
}
