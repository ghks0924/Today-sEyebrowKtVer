package com.example.today_seyebrowktver.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.today_seyebrowktver.databinding.ActivitySendMessageBinding
import java.text.SimpleDateFormat
import java.util.*

class ActivitySendMessage : ActivityBase() {

    private val REQUEST_PICK_CUSTOMERS: Int = 1111
    private lateinit var binding: ActivitySendMessageBinding

    private lateinit var oldMessageTitle: String
    private lateinit var oldMessageContent: String
    private lateinit var oldMessageDate: String
    private lateinit var messageType:String
    private lateinit var newMessageTitle: String
    private lateinit var newMessageContent: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLayout()
    }

    private fun setLayout() {

        val intent = intent
        messageType = intent.getStringExtra("type").toString()
        oldMessageTitle = intent.getStringExtra("title").toString()
        oldMessageContent = intent.getStringExtra("content").toString()
        oldMessageDate = intent.getStringExtra("date").toString()

        binding.messageTitleDisplayTv.setText(oldMessageTitle)
        binding.messageContentEt.setText(oldMessageContent)
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
        binding.backIv.setOnClickListener(View.OnClickListener {
            newMessageContent = binding.messageContentEt.text.toString().trim()
            newMessageTitle = binding.messageTitleDisplayTv.text.toString().trim()


            mUpdateMessage()


        })

        //send_btn event
        binding.backIv.setOnClickListener(View.OnClickListener {
            val Sharing_intent = Intent(Intent.ACTION_SEND)
            Sharing_intent.type = "text/plain"

            val Test_Message = "????????? Text"

            Sharing_intent.putExtra(Intent.EXTRA_TEXT, Test_Message)

            val Sharing = Intent.createChooser(Sharing_intent, "????????????")
            startActivity(Sharing)
        })
    }

    private fun mUpdateMessage() {
        if (newMessageContent == oldMessageContent){ //?????? ????????? ????????? ??????
            finish()
        } else {

            val ab: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
            ab.setMessage("????????? ?????? ????????? ?????????????????????????")
            ab.setPositiveButton("???", DialogInterface.OnClickListener { dialog, which ->

                // ??????????????? msec ?????? ?????????.
                val now = System.currentTimeMillis()
                // ??????????????? date ????????? ????????????.
                val date = Date(now)
                // ????????? ????????? ????????? ????????? ( yyyy/MM/dd ?????? ????????? ?????? ?????? )
                val sdfNow = SimpleDateFormat("yyyyMMddHHmmss")
                // nowDate ????????? ?????? ????????????.
                val formatDate = sdfNow.format(date)

                val intent = Intent()
                intent.putExtra("type", messageType)
                intent.putExtra("title", newMessageTitle)
                intent.putExtra("content", newMessageContent)
                intent.putExtra("newDate", formatDate)
                intent.putExtra("oldDate", oldMessageDate)
                setResult(RESULT_OK, intent)
                finish()

            })
            ab.setNegativeButton("?????????", DialogInterface.OnClickListener { dialog, which ->
                finish()
            })
            ab.setCancelable(true)
            ab.show()
        }
    }

    override fun onBackPressed() {
        newMessageContent = binding.messageContentEt.text.toString().trim()
        newMessageTitle = binding.messageTitleDisplayTv.text.toString().trim()


        mUpdateMessage()
    }
}