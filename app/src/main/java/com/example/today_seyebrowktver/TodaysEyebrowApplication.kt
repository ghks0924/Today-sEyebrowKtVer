package com.example.today_seyebrowktver

import android.app.Application

class TodaysEyebrowApplication :Application(){

    override fun onCreate() {
        super.onCreate()
        TotalRepository.initialize(this)
    }
}