package com.example.today_seyebrowktver.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.today_seyebrowktver.data.MessageData

@Database(entities = [MessageData::class], version = 1)
abstract class MessageDatabase :RoomDatabase() {

    abstract fun getMessageDao() : MessageDao

    companion object{

        private var INSTANCE: MessageDatabase? = null

        fun getInstance(context : Context) : MessageDatabase? {
            if(INSTANCE == null){
                synchronized(MessageDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MessageDatabase::class.java,
                        "memos.db")
                        .build()
                }
            }
            return INSTANCE
        }
    }

}