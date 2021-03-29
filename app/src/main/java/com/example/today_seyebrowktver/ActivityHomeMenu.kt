package com.example.today_seyebrowktver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.today_seyebrowktver.databinding.ActivityHomeMenuBinding

class ActivityHomeMenu : AppCompatActivity() {

    //viewBinding
    private lateinit var binding: ActivityHomeMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        overridePendingTransition(R.anim.horizon_enter, R.anim.fadeout)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing){
            overridePendingTransition(R.anim.fadein, R.anim.horizon_exit)
        }


    }
}