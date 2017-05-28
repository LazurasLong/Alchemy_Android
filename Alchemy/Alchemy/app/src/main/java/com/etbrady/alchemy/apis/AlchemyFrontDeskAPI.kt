package com.etbrady.alchemy.apis

import com.etbrady.alchemy.models.Event
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AlchemyFrontDeskAPI {

    @GET("front/event_occurrences.json?client_id=WWgvG1fId8iDU3rgoFXvz4A2kLnxDBSsOFacfk8X")
    fun getEvents(@Query("from") from: String, @Query("to") to: String): Call<List<Event>>
}

