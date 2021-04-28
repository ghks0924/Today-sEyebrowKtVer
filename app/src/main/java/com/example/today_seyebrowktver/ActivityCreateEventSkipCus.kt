package com.example.today_seyebrowktver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.today_seyebrowktver.databinding.ActivityCreateEventSkipCusBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ActivityCreateEventSkipCus : ActivityBase() {

    //viewBinding
    private lateinit var binding: ActivityCreateEventSkipCusBinding
    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    val mAuth = FirebaseAuth.getInstance()
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventSkipCusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = mAuth.currentUser
        uid = user.uid

        setLayout()
    }

    private fun setLayout() {
        mChangeStatusBarColor("#ebbdc5")
        binding.okBtn.setOnClickListener {
            createEvent()
        }
    }

    private fun createEvent() {
        //키값 생성
        val key = database.child("users").child(uid).child("events")
            .child("2021-04").push().key

        var newEvent = EventData("2021-04-30", "15:30", false, "김순자", "01030773637",
            "new", false, "기본", 50000, "not", "없음", "2021-04-29", key.toString())


        database.child("users").child(uid).child("events")
            .child("2021-04").child(key!!).setValue(newEvent)

    }

    private fun isValid(): Boolean {
        if (!binding.newRb.isChecked && !binding.retouchRb.isChecked) {
            return false
        }

//        if ()
        return true
    }
}