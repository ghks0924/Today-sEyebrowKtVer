package com.example.today_seyebrowktver.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.today_seyebrowktver.TotalRepository
import com.example.today_seyebrowktver.data.MemoData

class ActivityCreateMemoViewModel(application: Application) : AndroidViewModel(application) {

    //repository version
    private val totalRepository = TotalRepository.get()

    fun addMemo(memo:MemoData){
        totalRepository.insertMemo(memo)
    }
}