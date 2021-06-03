package com.example.today_seyebrowktver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.today_seyebrowktver.databinding.ActivityEditMessageGroupBinding

class ActivityEditMessageGroup : AppCompatActivity() {

    private lateinit var binding : ActivityEditMessageGroupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMessageGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    fun onClick(view: View) {
        when(view){
            binding.backIv -> finish()
        }
    }

}