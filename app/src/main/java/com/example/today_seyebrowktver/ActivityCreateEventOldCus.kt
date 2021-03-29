package com.example.today_seyebrowktver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.today_seyebrowktver.databinding.ActivityCreateEventOldCusBinding

class ActivityCreateEventOldCus : AppCompatActivity() {

    //viewBinding
    private lateinit var binding: ActivityCreateEventOldCusBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventOldCusBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}