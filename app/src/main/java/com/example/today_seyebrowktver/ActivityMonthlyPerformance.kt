package com.example.today_seyebrowktver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.today_seyebrowktver.databinding.ActivityMonthlyPerformanceBinding

class ActivityMonthlyPerformance : ActivityBase() {

    //viewBinding
    private lateinit var binding: ActivityMonthlyPerformanceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthlyPerformanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLayout()
    }

    private fun setLayout() {
        binding.backIv.setOnClickListener {
            finish()
        }
    }
}