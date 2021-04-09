package com.example.today_seyebrowktver

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.Room

class ViewModelMain(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(application,
            MemoDatabase::class.java, "memos").build()


    fun getAll(): LiveData<List<MemoData>> {
        return db.getMemeDao().getAllMemos()
    }

    suspend fun insert(memoData: MemoData) {
        db.getMemeDao().insert(memoData)
    }

    suspend fun delete(memoData: MemoData) {
        db.getMemeDao().delete(memoData)
    }

    suspend fun findByDate(date: String): MemoData {
        val memoData = db.getMemeDao().findByDate(date)
        return memoData
    }

    suspend fun update(memoData: MemoData){
        db.getMemeDao().update(memoData)
    }



}