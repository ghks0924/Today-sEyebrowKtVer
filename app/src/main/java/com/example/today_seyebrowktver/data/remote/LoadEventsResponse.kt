package com.example.today_seyebrowktver.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LoadEventsResponse (
    val data : List<EventsTest>

)

data class EventsTest(
    val name:String,
    val number: String
)