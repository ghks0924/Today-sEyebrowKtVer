package com.example.today_seyebrowktver

import androidx.annotation.Keep
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Keep
data class EventData(
    val date: String,
    val time: String,
    val complete: Boolean,
    val customerName: String,
    val customerNumber: String,
    val customerGrade: String,
    val isRetouch: Boolean,
    val menu: String,
    val price: String,
    val payment: String,
    val reservMemo: String,
    val savedate: String
)