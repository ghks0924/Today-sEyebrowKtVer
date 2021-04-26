package com.example.today_seyebrowktver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.today_seyebrowktver.databinding.ActivityEachCustomerBinding

class ActivityEachCustomer : ActivityBase() {

    private lateinit var binding: ActivityEachCustomerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEachCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}