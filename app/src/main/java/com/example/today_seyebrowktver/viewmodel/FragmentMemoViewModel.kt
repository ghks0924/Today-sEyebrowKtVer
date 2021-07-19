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

private val TAG = "FragmentMemoViewModel"
class FragmentMemoViewModel(application: Application) : AndroidViewModel(application) {

    var memos : List<MemoData>?=null

    val memoTitle = MutableLiveData<String>()
    val memoContent = MutableLiveData<String>()
    val memoDate = MutableLiveData<String>()

    fun sendMemoData(title: String, content: String, date: String) {
        memoTitle.value = title
        memoContent.value = content
        memoDate.value = date
    }

    //repository version
    private val totalRepository = TotalRepository.get()
    val memoListLiveData = totalRepository.getMemos()
    fun deleteMemo(memo : MemoData){
        totalRepository.deleteMemo(memo)
    }
}