package com.example.today_seyebrowktver.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Room
import com.example.today_seyebrowktver.TotalRepository
import com.example.today_seyebrowktver.data.MemoData
import com.example.today_seyebrowktver.room.MemoDao
import com.example.today_seyebrowktver.room.MemoDatabase
import com.example.today_seyebrowktver.ui.FragmentMemo


class MemoFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "MemoFragmentViewModel"

    var memos : List<MemoData>?=null

    val memoTitle = MutableLiveData<String>()
    val memoContent = MutableLiveData<String>()
    val memoDate = MutableLiveData<String>()

    //Room memoDB
    private val memoDB = Room.databaseBuilder(
        application,
        MemoDatabase::class.java, "memos"
    ).build()

    fun sendMemoData(title: String, content: String, date: String) {
        memoTitle.value = title
        memoContent.value = content
        memoDate.value = date
    }

    fun getAll(): LiveData<List<MemoData>> {
        return memoDB.getMemeDao().getAllMemos()
    }

    suspend fun insert(memoData: MemoData) {
        memoDB.getMemeDao().insert(memoData)
    }

    suspend fun delete(memoData: MemoData) {
        memoDB.getMemeDao().delete(memoData)
    }

    suspend fun findMemoByDate(date: String): MemoData {
        val memoData = memoDB.getMemeDao().findByDate(date)
        return memoData
    }

    suspend fun update(memoData: MemoData) {
        memoDB.getMemeDao().update(memoData)
    }

    private val totalRepository = TotalRepository.get()

    val memoListLiveData = totalRepository.getMemos()
}