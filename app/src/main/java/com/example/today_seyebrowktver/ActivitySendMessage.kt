package com.example.today_seyebrowktver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.today_seyebrowktver.databinding.ActivitySendMessageBinding

class ActivitySendMessage : ActivityBase() {

    private val REQUEST_PICK_CUSTOMERS: Int = 1111
    private lateinit var binding: ActivitySendMessageBinding

    private lateinit var mainViewModel: ViewModelMain

    private lateinit var messageType: String
    private lateinit var messageTitle: String
    private lateinit var messageContent: String
    private lateinit var messageDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendMessageBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setLayout()
    }

    private fun setLayout() {

        val intent = intent
        var messageContent = intent.getStringExtra("content")
        binding.messageContentEt.setText(messageContent)
        binding.messageContentEt.clearFocus()
        binding.parentLayout.requestFocus()

        binding.selectCustomerTv.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ActivityLoadCustomers::class.java)
            startActivityForResult(intent, REQUEST_PICK_CUSTOMERS)
        })

//        if (intent.hasExtra("type") &&
//            intent.hasExtra("title") &&
//            intent.hasExtra("content") &&
//            intent.hasExtra("date")
//        ) {
//            messageType = intent!!.getStringExtra("type")
//            messageTitle = intent!!.getStringExtra("title")
//            messageContent = intent!!.getStringExtra("content")
//            messageDate = intent!!.getStringExtra("date")
//        }
//
//        binding.messageTitleEt.setText(messageTitle)
//        binding.messageContentEt.setText(messageContent)


        //back_iv event
        binding.backCardview.setOnClickListener(View.OnClickListener {
            finish()
        })

        //send_btn event
        binding.sendIv.setOnClickListener(View.OnClickListener {

        })
    }
}