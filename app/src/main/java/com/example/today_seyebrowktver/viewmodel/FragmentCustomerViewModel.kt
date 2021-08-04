package com.example.today_seyebrowktver.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.today_seyebrowktver.TotalRepository

private const val TAG = "FragmentCustomerViewModel"
class FragmentCustomerViewModel(application: Application) : AndroidViewModel(application) {


    private val totalRepository = TotalRepository.get()
    var customerList = totalRepository.getCustomersList()

}