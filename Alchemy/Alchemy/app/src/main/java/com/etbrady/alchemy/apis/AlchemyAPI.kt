package com.etbrady.alchemy.apis

import com.etbrady.alchemy.models.Class
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query

interface AlchemyAPI {

    @FormUrlEncoded
    @GET("front/event_occurences.json")
    fun getClasses(@Query("from") from: String, @Query("to") to: String): List<Class>

}

