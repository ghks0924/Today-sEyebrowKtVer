package com.example.today_seyebrowktver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.today_seyebrowktver.data.remote.Api
import com.example.today_seyebrowktver.databinding.ActivityHomeMenuBinding
import com.google.android.material.snackbar.Snackbar

class ActivityHomeMenu : AppCompatActivity() {

    //viewBinding
    private lateinit var binding: ActivityHomeMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        overridePendingTransition(R.anim.horizon_enter, R.anim.fadeout)

        binding.et1.setText("menu")
        binding.et2.setText("기본")

        binding.btn.setOnClickListener {
            Api().loadEvents(
                binding.et1.text.toString(),
                binding.et2.text.toString()
            ) { isSuccess, data ->
                callback(isSuccess, data) {
                }
            }
        }

        binding.pcBtn.setOnClickListener {
            Api().findPcroom(
                binding.et1.text.toString(),
                binding.et2.text.toString()
            ) { isSuccess, data ->
                callback(isSuccess, data) {
                }
            }
        }

        binding.idcheckBtn.setOnClickListener {
            Api().idCheck(
               ) { isSuccess, data ->
                callback(isSuccess, data) {
                }
            }
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.fadein, R.anim.horizon_exit)
        }

    }


    private fun callback(success: Boolean, data: Any?, function: () -> Unit) {
        if (success) {
            Snackbar.make(binding.root, "Success", Snackbar.LENGTH_SHORT)
            Log.d("function", "success")
        } else {
            Snackbar.make(binding.root, "fial", Snackbar.LENGTH_SHORT)
            Log.d("function", "fail")
        }

        binding.showTv.text = data.toString()
    }


}