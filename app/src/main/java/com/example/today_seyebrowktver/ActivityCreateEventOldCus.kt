package com.example.today_seyebrowktver

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.today_seyebrowktver.databinding.ActivityCreateEventOldCusBinding

class ActivityCreateEventOldCus : ActivityBase() {

    //viewBinding
    private lateinit var binding: ActivityCreateEventOldCusBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventOldCusBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setLayout()


    }



    private fun setLayout() {
       mChangeStatusBarColor("#ebbdc5")

        binding.loadCusData.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, ActivityLoadCustomers::class.java)
            startActivity(intent)
        })
    }


}