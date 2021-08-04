package com.example.today_seyebrowktver.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.today_seyebrowktver.TotalRepository

class FragmentMessageViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "FragmentMessageViewModel"

    private val totalRepository = TotalRepository.get()
    var groupList = totalRepository.getMessageGroupList()

}