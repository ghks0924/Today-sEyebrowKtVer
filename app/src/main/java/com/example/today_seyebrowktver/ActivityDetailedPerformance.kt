package com.example.today_seyebrowktver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.today_seyebrowktver.databinding.ActivityDetailedPerformanceBinding
import com.example.today_seyebrowktver.databinding.ActivityMonthlyPerformanceBinding

class ActivityDetailedPerformance : ActivityBase() {

    //viewBinding
    private lateinit var binding: ActivityDetailedPerformanceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedPerformanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLayout()
    }

    private fun setLayout() {

    }
}