package com.example.today_seyebrowktver.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.today_seyebrowktver.databinding.ActivityEachMessageBinding

private const val TAG = "EACH_MESSAGE"
class ActivityEachMessage : ActivityBase() {

    //viewBinding
    private lateinit var binding: ActivityEachMessageBinding

    //이전 액티비티에서 받아온 message 객체의 데이터
    private lateinit var messageTitle: String
    private var messageContent : String ?= null
    private lateinit var messageDate: String
    private lateinit var messageType: String
    private lateinit var keyValue: String

    val activityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            messageType = intent?.getStringExtra("edittedType").toString()
            messageTitle = intent?.getStringExtra("edittedTitle").toString()
            messageContent = intent?.getStringExtra("edittedContent").toString()

            binding.messageTitleTv.text = messageTitle
            binding.messageContentTv.text = messageContent

            Log.d("intentTag", "new way is working")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEachMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLayout()
    }

    private fun setLayout() {
        val intent = intent
        messageTitle = intent.getStringExtra("title").toString()
        messageContent = intent.getStringExtra("content").toString()
        messageDate = intent.getStringExtra("date").toString()
        messageType = intent.getStringExtra("type").toString()
        Log.d(TAG, messageType+"?")
        keyValue = intent.getStringExtra("key").toString()

        binding.messageTitleTv.text = messageTitle
        binding.messageContentTv.text = messageContent

        //backIv 클릭이벤트
        binding.backIv.setOnClickListener {
            finish()
        }

        binding.editIv.setOnClickListener {
            mShowShortToast("수정")
            val mIntent = Intent(this@ActivityEachMessage, ActivityUpdateMessage::class.java)
            mIntent.putExtra("title", messageTitle)
            mIntent.putExtra("content", messageContent)
            mIntent.putExtra("type", messageType)
            mIntent.putExtra("date", messageDate)
            mIntent.putExtra("keyValue", keyValue)

            activityForResult.launch(mIntent)
        }

        binding.sendMessageButton.setOnClickListener {
            shareMessage(messageContent!!)
        }
    }

    //공유기능버튼
    private fun shareMessage(content: String) {
        //null check해주고
        if (content.isNullOrEmpty()) {
            mShowShortToast("공유할 메세지가 비어있습니다")
        } else {
            val Sharing_intent = Intent(Intent.ACTION_SEND)
            Sharing_intent.type = "text/plain"


            val Test_Message = content

            Sharing_intent.putExtra(Intent.EXTRA_TEXT, Test_Message)

            val Sharing = Intent.createChooser(Sharing_intent, "메세지 전송하기")
            startActivity(Sharing)

        }

    }

}