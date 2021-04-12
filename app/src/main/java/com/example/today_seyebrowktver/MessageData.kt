package com.example.today_seyebrowktver

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageData(
    @ColumnInfo(name = "type") var messageType: String,
    @ColumnInfo(name = "title") var messageTitle: String,
    @ColumnInfo(name = "content") var messageContent: String,
    @ColumnInfo(name = "date") var messageDate: String

) {
    @PrimaryKey(autoGenerate = true)
    var idx: Int = 0
}