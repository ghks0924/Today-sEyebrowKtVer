package com.example.today_seyebrowktver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.today_seyebrowktver.databinding.ActivityCreateMessageBinding
import java.text.SimpleDateFormat
import java.util.*

class ActivityCreateMessage : ActivityBase() {

    private lateinit var binding: ActivityCreateMessageBinding

    lateinit var tempType : String
    lateinit var tempTitle : String
    lateinit var tempContent : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLayout()
    }

    private fun setLayout() {

        //back_iv 뒤로가기 event
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })

        binding.selectTypeTv.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext, binding.selectTypeTv.text, Toast.LENGTH_SHORT).show()
        })

        //save button event
        binding.saveMessageButton.setOnClickListener(View.OnClickListener {
            tempType = binding.selectTypeTv.text.toString().trim()
            tempTitle = binding.messageTitleEt.text.toString().trim() //제목
            tempContent = binding.messageContentEt.text.toString().trim()

            if (vaildCheck()){
                // 현재시간을 msec 으로 구한다.
                val now = System.currentTimeMillis()
                // 현재시간을 date 변수에 저장한다.
                val date = Date(now)
                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                val sdfNow = SimpleDateFormat("yyyyMMddHHmmss")
                // nowDate 변수에 값을 저장한다.
                val formatDate = sdfNow.format(date) //시간

                val intent = Intent()
                intent.putExtra("type", "문자유형")
                intent.putExtra("title", tempTitle)
                intent.putExtra("content", tempContent)
                intent.putExtra("date", formatDate)
                setResult(RESULT_OK, intent)
                finish()
            }
        })
    }

    private fun vaildCheck() : Boolean{
//        if (tempType.isNullOrEmpty()){
//            mShowShortToast("문자유형을 선택해주세요")
//            return false
//        }

        if (tempTitle.isNullOrEmpty()){
            mShowShortToast("문자 제목을 입력해주세요")
            return false
        }

        if (tempContent.isNullOrEmpty()){
            mShowShortToast("문자 내용을 입력해주세요")
            return false
        }

        return  true
    }
}