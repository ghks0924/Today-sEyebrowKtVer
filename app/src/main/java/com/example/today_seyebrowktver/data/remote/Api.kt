package com.example.today_seyebrowktver.data.remote

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Api {
    companion object {
        val client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://us-central1-today-s-eyebrow-kt-ver.cloudfunctions.net/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

    }

    fun findPcroom(type: String, keyword: String, callback: (Boolean, Any?) -> Unit) {
        retrofit.create(FirebaseApi::class.java).findPcroom(type, keyword)
            .enqueue(object : Callback<ArrayList<findPcroomResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<findPcroomResponse>>,
                    response: Response<ArrayList<findPcroomResponse>>,
                ) {
                    callback(true, response.body())
                }

                override fun onFailure(call: Call<ArrayList<findPcroomResponse>>, t: Throwable) {
                    callback(false, t.message)
                }

            })
    }

    fun loadEvents(type: String, keyword: String,
                   year:String, month:String, callback: (Boolean, Any?) -> Unit) {
        retrofit.create(FirebaseApi::class.java).loadEvents(type, keyword, year, month)
            .enqueue(object : Callback<LoadEventsResponse2> {
                override fun onResponse(
                    call: Call<LoadEventsResponse2>,
                    response: Response<LoadEventsResponse2>,
                ) {
                    callback(true, response.body())
                }
                override fun onFailure(call: Call<LoadEventsResponse2>, t: Throwable) {
                    callback(false, t.message)
                }
            })
    }

    fun loadCustomers(
        type: String, keyword: String,
        type2: String, keyword2: String, callback: (Boolean, Any?) -> Unit,
    ) {
        retrofit.create(FirebaseApi::class.java).loadCustomers(type, keyword, type2, keyword2)
            .enqueue(object : Callback<List<LoadCustomersResponse>> {
                override fun onResponse(
                    call: Call<List<LoadCustomersResponse>>,
                    response: Response<List<LoadCustomersResponse>>,
                ) {
                    callback(true, response.body())
                }

                override fun onFailure(call: Call<List<LoadCustomersResponse>>, t: Throwable) {
                    callback(false, t.message)
                    Log.d("function fail", t.message)
                }

            })
    }


}