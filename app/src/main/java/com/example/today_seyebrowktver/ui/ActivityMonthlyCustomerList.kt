package com.example.today_seyebrowktver.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.today_seyebrowktver.R
import com.example.today_seyebrowktver.databinding.ActivityMonthlyCustomerListBinding
import com.google.android.material.tabs.TabLayout

class ActivityMonthlyCustomerList : AppCompatActivity() {

    //viewbinding
    private lateinit var binding : ActivityMonthlyCustomerListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthlyCustomerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }
}