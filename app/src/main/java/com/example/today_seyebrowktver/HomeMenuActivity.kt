package com.example.today_seyebrowktver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class HomeMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_menu)

        overridePendingTransition(R.anim.horizon_enter, R.anim.fadeout)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing){
            overridePendingTransition(R.anim.fadein, R.anim.horizon_exit)
        }


    }
}