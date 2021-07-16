package com.example.today_seyebrowktver.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.today_seyebrowktver.TotalRepository

class FragmentCalendarViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "FragmentCalendarViewModel"

    private val totalRepository = TotalRepository.get()

}