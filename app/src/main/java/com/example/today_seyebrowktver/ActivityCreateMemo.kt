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
    private lateinit var nowMemoTitle: String
    private lateinit var nowMemoContent: String
    private lateinit var nowMemoDate: String
    private var wasRevised = false
    private lateinit var type: String

    private lateinit var memoTitleFromPrevAct: String
    private lateinit var memoDateFromPrevAct: String
    private lateinit var memoContentFromPrevAct: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLayout()
    }

    private fun setLayout() {

        //이전 액티비티에서 데이터 받아오기 확인
        val intent = intent
        type = intent.getStringExtra("type")
        memoTitleFromPrevAct = intent.getStringExtra("memoTitle") ?: "null"
        memoDateFromPrevAct = intent.getStringExtra("memoDate") ?: "null"
        memoContentFromPrevAct = intent.getStringExtra("memoContent") ?: "null"


        //처음 세팅때 new vs update 확인 UI 세팅
        when (type) {
            "new" -> { //새로만드는거면
            }

            "update" -> {
                if (intent.hasExtra("memoTitle")) {
                    binding.memoTitleEt.setText(memoTitleFromPrevAct)
                }
                if (intent.hasExtra("memoDate")) {
                    binding.contentEdittext.setText(memoContentFromPrevAct)
                }
            }
        }


        //backIv 클릭이벤트
        binding.backCardview.setOnClickListener(View.OnClickListener {

            nowMemoTitle = binding.memoTitleEt.text.toString().trim()
            nowMemoContent = binding.contentEdittext.text.toString().trim()


            Log.d("type", type + "처음?")
            //여기서 update인지 create인지 파악하기
            when (type) {
                "new" -> {
                    mCreateMemo()
                    mKeyboardDown()
                }

                "update" -> {
                    mUpdateMemo()
                    mKeyboardDown()
                }
            }


        })

        //contentEditText에 포커스 주고 키보드 업
        binding.contentEdittext.requestFocus()
        mKeyboardUp()


        //내용만이라도 수정됐는지 확인해서 wasRevised = true로 바꿔줌
        binding.contentEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                wasRevised = true
                Log.d("wasRevised", "content : " + wasRevised)
            }

            override fun afterTextChanged(s: Editable) {}
        })

        //제목만이라도 수정됐는지 확인해서 wasRevised = true로 바꿔줌
        binding.memoTitleEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                wasRevised = true
                Log.d("wasRevised", "title : " + wasRevised)
            }

            override fun afterTextChanged(s: Editable?) {}

        })

    }

    override fun onBackPressed() {

        nowMemoTitle = binding.memoTitleEt.text.toString().trim()
        nowMemoContent = binding.contentEdittext.text.toString().trim()

        Log.d("type", type + "?")


        //여기서 update인지 create인지 파악하기
        when (type) {
            "new" -> {
                mCreateMemo()
            }

            "update" -> {
                mUpdateMemo()
            }
        }
    }

    //메모 업데이트 처리
    private fun mUpdateMemo() {
        //아무것도 수정도 안했거나 수정했어도 이전과 내용이 달라진게 없으면
        if ((nowMemoTitle == memoTitleFromPrevAct && nowMemoContent == memoContentFromPrevAct)) {
            wasRevised = false
            mKeyboardDown()
            finish()
        } else { //무언가가 수정이 됐어
            if (wasRevised && (nowMemoTitle != memoTitleFromPrevAct || nowMemoContent != memoContentFromPrevAct)) {
                //키보드 내리고 포커스 클리어
                binding.contentEdittext.clearFocus()
                binding.memoTitleEt.clearFocus()
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

                val intent = Intent()
                intent.putExtra("memoTitle", nowMemoTitle)
                intent.putExtra("memoContent", nowMemoContent)
                intent.putExtra("prevDate", memoDateFromPrevAct)
                intent.putExtra("memoDate", formatDate)
                intent.putExtra("type", type)
                setResult(RESULT_OK, intent)
                finish()

            }
        }
    }

    //메모 생성시 처리
    private fun mCreateMemo() {
        //CreateMemo 일 때
        if (nowMemoTitle.isEmpty() && nowMemoContent.isEmpty()) { //들어와서 아무것도 입력 안하고 나갈 때
            mKeyboardDown()
            finish()
        } else { //무언가를 입력했음
            if (wasRevised) {
                //키보드 내리고 포커스 클리어
                binding.contentEdittext.clearFocus()
                binding.memoTitleEt.clearFocus()
                binding.parentLayout.requestFocus() //기본 포커스 줘서 edittext에 포커스 안주기

                wasRevised = false

            } else {

                Log.d("getMemo", "update? :")
                //메인 액티비티로 저장할 메모 데이터와 RESULT_OK 보내기

                // 현재시간을 msec 으로 구한다.
                val now = System.currentTimeMillis()
                // 현재시간을 date 변수에 저장한다.
                val date = Date(now)
                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                val sdfNow = SimpleDateFormat("yyyyMMddHHmmss")
                // nowDate 변수에 값을 저장한다.
                val formatDate = sdfNow.format(date)

                val intent = Intent()
                intent.putExtra("memoTitle", nowMemoTitle)
                intent.putExtra("memoContent", nowMemoContent)
                intent.putExtra("memoDate", formatDate)
                intent.putExtra("type", type)
                setResult(RESULT_OK, intent)
                finish()

            }

        }
    }
}

