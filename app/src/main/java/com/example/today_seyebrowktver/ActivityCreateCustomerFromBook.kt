package com.example.today_seyebrowktver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.today_seyebrowktver.databinding.ActivityCreateCustomerFromBookBinding

class ActivityCreateCustomerFromBook : AppCompatActivity() {

    //viewBinding
    private lateinit var binding:ActivityCreateCustomerFromBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCustomerFromBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}