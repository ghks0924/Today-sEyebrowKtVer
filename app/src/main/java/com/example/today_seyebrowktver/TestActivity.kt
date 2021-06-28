package com.example.today_seyebrowktver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.today_seyebrowktver.data.remote.Api
import com.example.today_seyebrowktver.databinding.ActivityTestBinding
import com.example.today_seyebrowktver.ui.ActivityBase

class TestActivity : ActivityBase() {
    //viewBinding
    private lateinit var binding:ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn.setOnClickListener {

            Api().loadEvents(
                "uid",mAuth.currentUser.uid,
                "2021","04")
            { isSuccess, data ->
                callback(isSuccess, data) {
                }
            }
        }

    }

    private fun callback(success: Boolean, data: Any?, function: () -> Unit) {
        if (success) {
            Log.d("function", "success")
        } else {
            Log.d("function", "fail")
        }

        binding.tv.text = data.toString() + mAuth.currentUser.uid
    }
}