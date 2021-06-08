package com.example.today_seyebrowktver.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FirebaseApi {

    @GET("/loadEvents")
    fun loadEvents(
        @Query("type") type:String,
        @Query("keyword") keyword:String
    ): Call<ArrayList<LoadEventsResponse>>

    @GET("/findPcroom")
    fun findPcroom(
        @Query("type") type:String,
        @Query("keyword") keyword: String
    ): Call<ArrayList<findPcroomResponse>>

    @GET("/loadEvents2")
    fun loadEvents2(
        @Query("type") type:String,
        @Query("keyword") keyword:String
    ): Call<ArrayList<LoadEventsResponse2>>
}