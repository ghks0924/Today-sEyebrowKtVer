package com.example.today_seyebrowktver.ui

import android.os.Bundle
import com.example.today_seyebrowktver.databinding.ActivityEachEventBinding

class ActivityEachEvent : ActivityBase() {

    private lateinit var binding:ActivityEachEventBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEachEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //팝업 액티비티의 크기 조절
        val dm = applicationContext.resources.displayMetrics
        val width = (dm.widthPixels * 0.9).toInt() // Display 사이즈의 90%
        val height = (dm.widthPixels * 0.8).toInt()
        window.attributes.width = width


    }
}