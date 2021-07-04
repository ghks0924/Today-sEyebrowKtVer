package com.example.today_seyebrowktver.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.today_seyebrowktver.R
import com.example.today_seyebrowktver.data.remote.Api
import com.example.today_seyebrowktver.databinding.ActivityHomeMenuBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class ActivityHomeMenu : ActivityBase() {

    //viewBinding
    private lateinit var binding: ActivityHomeMenuBinding

    val mAuth2 = FirebaseAuth.getInstance()
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val typeTest = "uid"
        uid = mAuth2.currentUser!!.uid

        overridePendingTransition(R.anim.horizon_enter, R.anim.fadeout)


        binding.btn.setOnClickListener {
            Api().loadEvents(
                "uid", uid,
                "2021",
                "04"
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





    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.fadein, R.anim.horizon_exit)
        }

    }

    private fun callback(success: Boolean, data: Any?, function: () -> Unit) {
        if (success) {
            Log.d("function", "success")
        } else {
            Log.d("function", "fail")
        }

        binding.showTv.text = data.toString()
    }

}