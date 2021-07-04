package com.example.today_seyebrowktver.data.remote

import androidx.annotation.Keep

@Keep
class LoadEventsResponse2 : ArrayList<LoadEventsResponse2.LoadEventsResponse2Item>(){
    @Keep
    data class LoadEventsResponse2Item(
        val name: String,
        val number: String
    )
}