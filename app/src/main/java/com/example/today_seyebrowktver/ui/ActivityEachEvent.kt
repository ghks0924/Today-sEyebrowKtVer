package com.example.today_seyebrowktver.ui

import android.os.Bundle
import com.example.today_seyebrowktver.databinding.ActivityEachEventBinding

class ActivityEachEvent : ActivityBase() {

    private lateinit var binding:ActivityEachEventBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEachEventBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}