package com.example.today_seyebrowktver.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.today_seyebrowktver.data.EventData

@Database(entities = [EventData::class], version = 1)
abstract class EventDatabase : RoomDatabase() {

    abstract fun getEventDao(): EventDao

    companion object {
        private var INSTANCE: EventDatabase? = null

        fun getInstance(context: Context): EventDatabase? {
            if (INSTANCE == null){
                synchronized(EventDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java,
                    "events.db").build()
                }
            }

            return INSTANCE
        }
    }
}