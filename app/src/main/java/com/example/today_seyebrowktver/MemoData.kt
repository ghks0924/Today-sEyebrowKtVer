package com.example.today_seyebrowktver

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Keep
@Entity(tableName = "memos")
data class  MemoData(
    @ColumnInfo(name = "date") var memoDate: String,
    @ColumnInfo(name = "title") var memoTitle: String,
    @ColumnInfo(name = "content") var memoContent: String
) {
    @PrimaryKey(autoGenerate = true)
    var idx: Int = 0
}
