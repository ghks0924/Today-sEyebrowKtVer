package com.example.today_seyebrowktver

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.today_seyebrowktver.data.MemoData
import com.example.today_seyebrowktver.room.MemoDatabase
import java.util.concurrent.Executors

private const val MEMO_DATABASE = "memo-database"

class TotalRepository private constructor(context: Context) {

    //Local DB

    //Background Thread executor
    private val executor = Executors.newSingleThreadExecutor()

    //memo RoomDB
    private val memoDB: MemoDatabase = Room.databaseBuilder(
        context.applicationContext,
        MemoDatabase::class.java,
        MEMO_DATABASE
    ).build()

    private val memoDao = memoDB.getMemeDao()

    fun getMemos(): LiveData<List<MemoData>> = memoDao.getAllMemos()

    fun insertMemo(memo: MemoData) {
        executor.execute {
            memoDao.insert(memo)
        }
    }

    fun deleteMemo(memo: MemoData) {
        executor.execute {
            memoDao.delete(memo)
        }
    }

    fun findMemo(date: String): MemoData {
        val memo = memoDao.findByDate(date)
        return memo
    }

    companion object {
        private var INSTANCE: TotalRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TotalRepository(context)
            }
        }

        fun get(): TotalRepository {
            return INSTANCE ?: throw IllegalStateException("TotalRepository must be initialized")
        }
    }

}