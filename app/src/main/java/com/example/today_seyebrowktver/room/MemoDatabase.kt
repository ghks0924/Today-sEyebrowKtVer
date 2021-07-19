package com.example.today_seyebrowktver.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.today_seyebrowktver.data.MemoData

@Database(entities = [MemoData::class], version = 4)
abstract class MemoDatabase : RoomDatabase() {

    abstract fun getMemeDao(): MemoDao

    companion object {

        private var INSTANCE: MemoDatabase? = null

        fun getInstance(context: Context): MemoDatabase? {
            if (INSTANCE == null) {
                synchronized(MemoDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MemoDatabase::class.java,
                        "memos.db"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }
    }

}