package com.example.today_seyebrowktver.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.example.today_seyebrowktver.EachMessageData
import com.example.today_seyebrowktver.databinding.ActivityUpdateMemoBinding
import com.example.today_seyebrowktver.databinding.ActivityUpdateMessageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class ActivityUpdateMessage : ActivityBase() {

    //viewBinding
    private lateinit var binding: ActivityUpdateMessageBinding

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var uid: String

    //이전 액티비티에서 받아온 메모 객체의 데이터
    private lateinit var prevMessageTitle: String
    private lateinit var prevMessageContent: String
    private lateinit var prevMessageDate: String
    private lateinit var prevMessageType: String
    private lateinit var keyValue: String

    //update할 메세지의 데이터
    private lateinit var newMsgTitle: String
    private lateinit var newMsgContent: String
    private var wasRevised = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = mAuth.currentUser
        uid = user.uid

        setLayout()
    }

    private fun setLayout() {
        val intent = intent
        prevMessageTitle = intent.getStringExtra("title")
        prevMessageContent = intent.getStringExtra("content")
        prevMessageDate = intent.getStringExtra("date")
        prevMessageType = intent.getStringExtra("type")
        keyValue = intent.getStringExtra("key")

        binding.messageTitleEt.setText(prevMessageTitle)
        binding.messageContentEt.setText(prevMessageContent)

        //backIv 클릭이벤트
        binding.backIv.setOnClickListener {
            newMsgTitle = binding.messageTitleEt.text.toString().trim()
            newMsgContent = binding.messageContentEt.text.toString().trim()

            if (newMsgTitle == prevMessageTitle && newMsgContent == prevMessageContent) {
                finish()
            } else {
                mUpdateMessage()
                mKeyboardDown()
            }

        }

        binding.messageTitleEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                wasRevised = true
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.messageContentEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                wasRevised = true
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })


    }

    private fun mUpdateMessage() {
        //무언가를 입력했음
        if (wasRevised) {
            //키보드 내리고 포커스 클리어
            binding.messageTitleEt.clearFocus()
            binding.messageContentEt.clearFocus()
            binding.parentLayout.requestFocus() //기본 포커스 줘서 edittext에 포커스 안주기

            wasRevised = false

        } else {

            //메인 액티비티로 저장할 메모 데이터와 RESULT_OK 보내기
            // 현재시간을 msec 으로 구한다.
            val now = System.currentTimeMillis()
            // 현재시간을 date 변수에 저장한다.
            val date = Date(now)
            // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
            val sdfNow = SimpleDateFormat("yyyyMMddHHmmss")
            // nowDate 변수에 값을 저장한다.
            val formatDate = sdfNow.format(date)

            val newMessageData = EachMessageData("시술후", newMsgTitle, newMsgContent, formatDate)

            database.child("users").child(uid).child("messages").child(keyValue)
                .setValue(newMessageData)
                .addOnSuccessListener {
                    Log.d("updateMsg", "success")

                }.addOnFailureListener{
                    Log.d("updateMsg", "fail")
                }

            finish()

        }
    }

    override fun onBackPressed() {
        newMsgTitle = binding.messageTitleEt.text.toString().trim()
        newMsgContent = binding.messageContentEt.text.toString().trim()

        if (newMsgTitle == prevMessageTitle && newMsgContent == prevMessageContent) {
            finish()
        } else {
            mUpdateMessage()
            mKeyboardDown()
        }
    }

}