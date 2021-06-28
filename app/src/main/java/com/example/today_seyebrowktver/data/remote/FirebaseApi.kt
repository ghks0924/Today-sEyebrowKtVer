package com.example.today_seyebrowktver.data.remote

import androidx.room.TypeConverter
import com.example.today_seyebrowktver.CustomersData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FirebaseApi {

    @GET("/findPcroom")
    fun findPcroom(
        @Query("type") type:String,
        @Query("keyword") keyword: String
    ): Call<ArrayList<findPcroomResponse>>

    @GET("loadEvents")
    fun loadEvents(
        @Query("type") type: String,
        @Query("keyword") keyword: String,
        @Query("year") year: String,
        @Query("month") month: String
        ):Call<List<LoadEventsResponse>>



    @GET("/loadCustomers")
    fun loadCustomers(
        @Query("type") type: String,
        @Query("keyword") keyword: String,
        @Query("type2") type2: String,
        @Query("keyword2") keyword2: String,
    ): Call<List<LoadCustomersResponse>>




}