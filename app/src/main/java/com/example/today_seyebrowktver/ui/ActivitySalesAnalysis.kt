package com.example.today_seyebrowktver.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.today_seyebrowktver.R
import com.example.today_seyebrowktver.databinding.ActivityMonthlyDetailedSalesBinding
import com.example.today_seyebrowktver.databinding.ActivitySalesAnalysisBinding

class ActivitySalesAnalysis : AppCompatActivity() {

    //viewBinding
    private lateinit var binding: ActivitySalesAnalysisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalesAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        binding.tv.text = intent.getStringExtra("cardcash")

    }
}