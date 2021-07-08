package com.example.today_seyebrowktver.data

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Keep
@Entity(tableName = "events")
data class EventData(
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "time") var time: String,
    @ColumnInfo(name = "complete") var complete: Boolean,
    @ColumnInfo(name = "customerName") var customerName: String,
    @ColumnInfo(name = "customerNumber") var customerNumber: String,
    @ColumnInfo(name = "customerGrade") var customerGrade: String,
    @ColumnInfo(name = "isRetouch") var isRetouch: Boolean,
    @ColumnInfo(name = "menu") var menu: String,
    @ColumnInfo(name = "price") var price: Int,
    @ColumnInfo(name = "payment") var payment: String,
    @ColumnInfo(name = "reservMemo") var reservMemo: String,
    @ColumnInfo(name = "savedate") var savedate: String,
    @ColumnInfo(name = "keyValue") var keyValue: String,
) {
    @PrimaryKey(autoGenerate = true)
    var idx: Int = 0
}