package com.example.today_seyebrowktver.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.today_seyebrowktver.data.SalesData

@Database(entities = [SalesData::class], version = 0)
abstract class SalesDatabase : RoomDatabase() {
}