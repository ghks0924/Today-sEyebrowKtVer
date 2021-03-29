package com.example.today_seyebrowktver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.today_seyebrowktver.databinding.ActivityCreateCustomerBinding

class ActivityCreateCustomer : AppCompatActivity() {

    //viewBinding
    private lateinit var binding: ActivityCreateCustomerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

}