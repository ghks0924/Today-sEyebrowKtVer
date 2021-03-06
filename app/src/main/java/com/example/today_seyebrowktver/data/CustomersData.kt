package com.example.today_seyebrowktver.data

import androidx.annotation.Keep
import java.lang.reflect.Constructor

@Keep
data class CustomersData(
    var customerName: String = "",
    var customerNumber: String = "",
    var customerMemo: String = "",
    var history: Int = 0,
    var grade: String = "",
    var savedate: String = "",
    var noshowCount: Int = 0,
    var keyValue: String = "",
    var sales : Int = 0,
    var photoURL : String = ""
)

