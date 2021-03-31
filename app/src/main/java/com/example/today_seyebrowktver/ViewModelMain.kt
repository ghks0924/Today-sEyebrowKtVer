package com.example.today_seyebrowktver

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.Room

class ViewModelMain(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(application,
    MemoDatabase::class.java, "memos").allowMainThreadQueries().fallbackToDestructiveMigration().build()



    fun getAll(): LiveData<List<MemoData2>> {
        return db.getMemeDao().getAllMemos()
    }

    fun insert(memoData: MemoData2){
        db.getMemeDao().insert(memoData)
    }

}