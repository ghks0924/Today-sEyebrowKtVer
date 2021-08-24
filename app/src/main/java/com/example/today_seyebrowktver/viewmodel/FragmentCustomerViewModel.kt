package com.example.today_seyebrowktver.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.today_seyebrowktver.TotalRepository
import com.example.today_seyebrowktver.data.CustomersData
import kotlinx.coroutines.launch

private const val TAG = "FragmentCustomerViewModel"
class FragmentCustomerViewModel(application: Application) : AndroidViewModel(application) {


    private val totalRepository = TotalRepository.get()
    var customerList = totalRepository.getCustomersList()

    // viewmodel보다 더 깊은 곳에서 끌어오는건 맞지 않다고 하여 사용 X
//    private val _liveData = MutableLiveData<List<CustomersData>>()
//    val liveData: LiveData<List<CustomersData>> get() = _liveData
//
//    fun loadData() = viewModelScope.launch {
//        _liveData.value = customerList
////        _liveData.postValue("value")

}