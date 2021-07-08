package com.example.today_seyebrowktver.data

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "sales")
data class SalesData(
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "complete") var complete: Boolean,
) {
    @PrimaryKey(autoGenerate = true)
    var idx: Int = 0
}