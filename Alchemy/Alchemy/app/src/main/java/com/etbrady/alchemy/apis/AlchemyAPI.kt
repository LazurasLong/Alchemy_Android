package com.etbrady.alchemy.apis

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AlchemyAPI {

    @GET("{date}")
    fun getWorkouts(@Path("date") date: String): Call<ResponseBody>
}