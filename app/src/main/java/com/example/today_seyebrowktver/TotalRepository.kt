package com.example.today_seyebrowktver

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.today_seyebrowktver.data.MemoData
import com.example.today_seyebrowktver.room.MemoDatabase

private const val MEMO_DATABASE = "memo-database"
class TotalRepository private constructor(context: Context){

    //Local DB

    //memo RoomDB
    private val memoDB : MemoDatabase = Room.databaseBuilder(
        context.applicationContext,
        MemoDatabase::class.java,
        MEMO_DATABASE
    ).build()

    private val memoDao = memoDB.getMemeDao()

    fun getMemos():LiveData<List<MemoData>> = memoDao.getAllMemos()

    companion object {
        private var INSTANCE: TotalRepository? = null

        fun initialize(context: Context){
            if (INSTANCE == null){
                INSTANCE = TotalRepository(context)
            }
        }

        fun get():TotalRepository {
            return INSTANCE?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }
    }

}