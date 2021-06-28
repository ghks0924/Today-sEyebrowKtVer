package com.example.today_seyebrowktver.data.remote

import androidx.annotation.Keep
import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName


@Keep
data class LoadCustomersResponse(

    val testArray : List<Customer>
)

data class Customer(
    val key : String,
    val name : String,
    val type : String,
)



