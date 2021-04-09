package com.example.today_seyebrowktver

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MemoData::class], version = 2)
abstract class MemoDatabase : RoomDatabase() {

    abstract fun getMemeDao() : MemoDao

    companion object{

        private var INSTANCE: MemoDatabase? = null

        fun getInstance(context : Context) : MemoDatabase? {
            if(INSTANCE == null){
                synchronized(MemoDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MemoDatabase::class.java,
                        "memos.db")
                        .build()
                }
            }
            return INSTANCE
        }
    }
}