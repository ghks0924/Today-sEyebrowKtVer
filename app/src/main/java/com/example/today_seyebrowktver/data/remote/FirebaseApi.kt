package com.example.today_seyebrowktver.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FirebaseApi {

    @GET("/loadEvents")
    fun loadEvents(
        @Query("type") a:String,
        @Query("keyword") b:String
    ): Call<ArrayList<LoadEventsResponse>>
}