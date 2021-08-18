package com.example.today_seyebrowktver.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.today_seyebrowktver.TotalRepository

private val TAG = "FragmentSalesViewModel"
class FragmentSalesViewModel(application: Application) : AndroidViewModel(application) {

    //current Month

    //view에 뿌려질 데이터를 보관하는 viewModel
    private val totalRepository = TotalRepository.get() //firebase로 쓰는 법은 잘 모르겠음

    //월매출 가져와야함


}