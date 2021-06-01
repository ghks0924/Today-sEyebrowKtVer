package com.example.today_seyebrowktver.data.remote

import com.example.today_seyebrowktver.App
import com.example.today_seyebrowktver.R
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

    fun loadEvents(type: String, keyword: String, callback:(Boolean, Any?) -> Unit){
        retrofit.create(FirebaseApi::class.java).loadEvents(type, keyword)
            .enqueue(object : Callback<ArrayList<LoadEventsResponse>>{
                override fun onResponse(
                    call: Call<ArrayList<LoadEventsResponse>>,
                    response: Response<ArrayList<LoadEventsResponse>>,
                ) {
                    callback(true, response.body())
                }

                override fun onFailure(call: Call<ArrayList<LoadEventsResponse>>, t: Throwable) {
                    callback(false, t.message)
                }

            })
    }
}