package com.example.today_seyebrowktver.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.today_seyebrowktver.TotalRepository

class FragmentSalesViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "FragmentSalesViewModel"

    private val totalRepository = TotalRepository.get()

}