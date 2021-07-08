package com.example.today_seyebrowktver.data

import androidx.annotation.Keep
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Keep
data class EventData(
    var date: String = "",
    var time: String = "",
    var complete: Boolean = false,
    var customerName: String = "",
    var customerNumber: String = "",
    var customerGrade: String = "",
    var isRetouch: Boolean = false,
    var menu: String = "",
    var price: Int = 0,
    var payment: String = "",
    var reservMemo: String = "",
    var savedate: String = "",
    var keyvarue: String = ""
)