package com.example.today_seyebrowktver.data.remote

import com.example.today_seyebrowktver.App
import com.example.today_seyebrowktver.R
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
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
            .baseUrl(App.applicationContext().resources.getString(R.string.firebase_functions_base_url))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }
}