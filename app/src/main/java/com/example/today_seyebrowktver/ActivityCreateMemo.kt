package com.example.today_seyebrowktver

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.example.today_seyebrowktver.databinding.ActivityCreateMemoBinding
import java.text.SimpleDateFormat
import java.util.*

class ActivityCreateMemo : ActivityBase() {

    //viewBinding
    private lateinit var binding: ActivityCreateMemoBinding

    //memo 객체를 받을 변수들
    private lateinit var memoTitle: String
    private lateinit var memoContent: String
    private lateinit var memoDate: String
    private var wasRevised = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLayout()
    }

    private fun setLayout() {
        //backIv 클릭이벤트
        binding.backCardview.setOnClickListener(View.OnClickListener {

            memoTitle = binding.memoTitleEt.text.toString().trim()
            memoContent = binding.contentEdittext.text.toString().trim()


            //여기서 update인지 create인지 파악하기


            //CreateMemo 일 때
            if (memoTitle.isEmpty() && memoContent.isEmpty()) { //들어와서 아무것도 입력 안하고 나갈 때
                /* 메인 액티비티로 RESULT_OK 보내야함
                */
                mKeyboardDown()
                finish()

            } else { //무언가를 입력했음
                if (wasRevised) {
                    //키보드 내리고 포커스 클리어
                    binding.contentEdittext.clearFocus()
                    binding.memoTitleEt.clearFocus()
                    binding.parentLayout.requestFocus()

                    mKeyboardDown()

                    wasRevised = false

                } else {

                    /* 메인 액티비티로 저장할 메모 데이터와 RESULT_OK 보
                     */

                    // 현재시간을 msec 으로 구한다.
                    val now = System.currentTimeMillis()
                    // 현재시간을 date 변수에 저장한다.
                    val date = Date(now)
                    // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                    val sdfNow = SimpleDateFormat("yyyyMMddHHmmss")
                    // nowDate 변수에 값을 저장한다.
                    val formatDate = sdfNow.format(date)

                    val intent = Intent()
                    intent.putExtra("memoTitle", memoTitle)
                    intent.putExtra("memoContent", memoContent)
                    intent.putExtra("memoDate", formatDate)
                    setResult(RESULT_OK, intent)

                    Log.d("wasRevised", "else " + wasRevised)
                    mShowShortToast("메인액티비로 고우")
                    finish()


                }

            }


        })

        //contentEditText에 포커스 주고 키보드 업
        binding.contentEdittext.requestFocus()
        mKeyboardUp()


        //내용만이라도 수정됐는지 확인
        binding.contentEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                wasRevised = true
                Log.d("wasRevised", "content : " + wasRevised)
            }

            override fun afterTextChanged(s: Editable) {}
        })

        //제목만이라도 수정됐는지
        binding.memoTitleEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                wasRevised = true
                Log.d("wasRevised", "title : " + wasRevised)
            }

            override fun afterTextChanged(s: Editable?) {}

        })

    }
}