package com.example.today_seyebrowktver.ui

import androidx.databinding.BaseObservable
import com.example.today_seyebrowktver.data.CustomersData

class CustomerViewModel :BaseObservable() {

    var customer: CustomersData?= null
        set(customer) {
            field = customer
            notifyChange()
        }

    //각 리스트에 보여줄 항목들
    val name: String? //고객이름
        get() = customer?.customerName

    val number: String? //고객번호
        get() = customer?.customerNumber

    val history: String? //고객 방문 히스토리
        get() = customer?.history.toString()+"회 방문"
}