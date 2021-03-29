package com.example.today_seyebrowktver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.today_seyebrowktver.databinding.ActivityCreateEventSkipCusBinding

class ActivityCreateEventSkipCus : AppCompatActivity() {

    //viewBinding
    private lateinit var binding: ActivityCreateEventSkipCusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventSkipCusBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}